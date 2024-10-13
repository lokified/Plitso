package com.loki.plitso.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.loki.plitso.util.Constants.SYNCDATAWORKER_NAME
import java.util.concurrent.TimeUnit

object WorkInitializer {

    const val IS_TESTING = false

    fun initializeDataSyncWork(context: Context) {
        val request = PeriodicWorkRequestBuilder<SyncDataWorker>(24, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(
                        NetworkType.CONNECTED
                    )
                    .build()
            )
            .build()
        WorkManager.getInstance(context).apply {
            enqueueUniquePeriodicWork(
                SYNCDATAWORKER_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }

    fun initializeDayRecipeWork(context: Context) {
        val timeUnit = if (IS_TESTING) TimeUnit.MINUTES else TimeUnit.HOURS

        val updateWorkRequest =
            PeriodicWorkRequestBuilder<DayRecipeWorker>(if (IS_TESTING) 15 else 24, timeUnit)
                .build()
        WorkManager.getInstance(context).enqueue(updateWorkRequest)
    }
}