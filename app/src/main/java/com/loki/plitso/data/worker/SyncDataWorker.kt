package com.loki.plitso.data.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.loki.plitso.R
import com.loki.plitso.data.repository.recipe.RecipeRepository
import com.loki.plitso.util.Constants.NOTIFICATION_CHANNEL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.random.Random

class SyncDataWorker(
    val context: Context,
    val workParams: WorkerParameters,
    private val recipeRepository: RecipeRepository
) : CoroutineWorker(context, workParams) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            Random.nextInt(),
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.splash_image)
                .setProgress(1, 1, true)
                .build()
        )
    }

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            recipeRepository.refreshDatabase()
        }
        return Result.success()
    }
}