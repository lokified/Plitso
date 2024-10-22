package com.loki.plitso.data.worker

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.loki.plitso.data.worker.WorkInitializer.SYNC_DATA_WORK_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate

class SyncDataManagerImpl(
    private val context: Context
) : SyncDataManager {

    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(SYNC_DATA_WORK_NAME)
            .map(MutableList<WorkInfo>::anyRunning)
            .asFlow()
            .conflate()


    override suspend fun startSync() {
        val syncDataRequest = OneTimeWorkRequestBuilder<SyncDataWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.beginUniqueWork(SYNC_DATA_WORK_NAME, ExistingWorkPolicy.KEEP, syncDataRequest)
            .enqueue()
    }
}

val List<WorkInfo>.anyRunning get() = any { it.state == WorkInfo.State.RUNNING }