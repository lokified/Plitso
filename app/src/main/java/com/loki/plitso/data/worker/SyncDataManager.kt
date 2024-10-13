package com.loki.plitso.data.worker

import kotlinx.coroutines.flow.Flow

interface SyncDataManager {

    val isSyncing: Flow<Boolean>
    suspend fun startSync()
}