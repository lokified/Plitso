package com.loki.plitso.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.loki.plitso.data.local.dao.DayRecipeDao
import com.loki.plitso.data.repository.recipe.RecipeRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncDataWorkerFactory : WorkerFactory(), KoinComponent {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val recipeRepository: RecipeRepository by inject()
        val dayRecipeDao: DayRecipeDao by inject()

        return when (workerClassName) {
            SyncDataWorker::class.java.name -> {
                SyncDataWorker(appContext, workerParameters, recipeRepository)
            }

            DayRecipeWorker::class.java.name -> {
                DayRecipeWorker(appContext, workerParameters, recipeRepository, dayRecipeDao)
            }

            else -> null
        }
    }
}