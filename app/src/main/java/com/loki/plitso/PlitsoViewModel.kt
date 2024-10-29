package com.loki.plitso

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.plitso.data.local.datastore.DatastoreStorage
import com.loki.plitso.data.local.datastore.LocalUser
import com.loki.plitso.data.local.datastore.Theme
import com.loki.plitso.data.network.NetworkMonitor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PlitsoViewModel(
    private val datastoreStorage: DatastoreStorage,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {
    val theme =
        datastoreStorage.getAppTheme().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            Theme.System.name,
        )

    val user =
        datastoreStorage.getLocalUser().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            LocalUser(),
        )

    val isNetworkAvailable =
        networkMonitor.networkStatus.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            true
        )
}
