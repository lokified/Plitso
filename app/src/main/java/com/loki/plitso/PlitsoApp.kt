package com.loki.plitso

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.Configuration
import com.loki.plitso.data.worker.SyncDataWorkerFactory
import com.loki.plitso.data.worker.WorkInitializer
import com.loki.plitso.di.aiModule
import com.loki.plitso.di.appModule
import com.loki.plitso.di.authModule
import com.loki.plitso.di.datastoreModule
import com.loki.plitso.di.repositoryModule
import com.loki.plitso.util.Constants.CHANNEL_NAME
import com.loki.plitso.util.Constants.NOTIFICATION_CHANNEL
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class PlitsoApp : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@PlitsoApp)
            modules(
                repositoryModule,
                appModule,
                datastoreModule,
                authModule,
                aiModule
            )
        }
        setUpWorkerManagerNotificationChannel()
        WorkInitializer.initializeDataSyncWork(this)
        WorkInitializer.initializeDayRecipeWork(this)
    }

    private fun initTimber() = when {
        BuildConfig.DEBUG -> {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return super.createStackElementTag(element) + ":" + element.lineNumber
                }
            })
        }

        else -> {
            Timber.plant(CrashlyticsTree())
        }
    }

    override val workManagerConfiguration: Configuration =
        Configuration.Builder()
            .setWorkerFactory(SyncDataWorkerFactory())
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    private fun setUpWorkerManagerNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notification for data updates"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}