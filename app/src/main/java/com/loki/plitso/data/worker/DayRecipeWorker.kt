package com.loki.plitso.data.worker

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.loki.plitso.MainActivity
import com.loki.plitso.R
import com.loki.plitso.data.local.dao.DayRecipeDao
import com.loki.plitso.data.remote.mealdb.mappers.toDayRecipe
import com.loki.plitso.data.repository.recipe.RecipeRepository
import com.loki.plitso.presentation.permission.checkIfPermissionGranted
import com.loki.plitso.util.Constants.NOTIFICATION_CHANNEL
import com.loki.plitso.util.Resource
import com.loki.plitso.util.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class DayRecipeWorker(
    val context: Context,
    val workParams: WorkerParameters,
    private val recipeRepository: RecipeRepository,
    private val dayRecipeDao: DayRecipeDao
) : CoroutineWorker(context, workParams) {

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            val recipes = dayRecipeDao.getRecipes().first()

            val lastUpdated = if (recipes.isNotEmpty()) recipes[0].updatedDate else Date()
            val currentTime = Date()

            if ((currentTime.time - lastUpdated.time) >= TimeUnit.HOURS.toMillis(24)) {
                recipeRepository.getRandomRecipe().collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            Timber.tag("day recipe random").d(result.message)
                        }

                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            val recipeRec = result.data
                            dayRecipeDao.clear()
                            dayRecipeDao.insert(recipeRec.toDayRecipe(currentTime))
                            sendNotification(
                                recipeRec.strMeal, recipeRec.strInstructions ?: "No Instructions"
                            )
                        }
                    }
                }
            }
        }

        return Result.success()
    }


    private fun sendNotification(recipeTitle: String, recipeInstructions: String) {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.splash_image)
            .setContentTitle("Recipe of the day: $recipeTitle").setContentText(recipeInstructions)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission(context as ComponentActivity)
                return
            }
            notify(Random.nextInt(), builder.build())
        }
    }

    private fun requestNotificationPermission(activity: ComponentActivity) {
        val requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                activity.showToast("Permission Granted")
            } else {
                activity.showToast("Permission Denied")
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}