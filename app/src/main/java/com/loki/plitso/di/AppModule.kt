package com.loki.plitso.di

import androidx.room.Room
import com.loki.plitso.PlitsoViewModel
import com.loki.plitso.data.local.PlitsoDatabase
import com.loki.plitso.data.remote.mealdb.MealdbApi
import com.loki.plitso.data.worker.SyncDataManager
import com.loki.plitso.data.worker.SyncDataManagerImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

val appModule = module {

    single<MealdbApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    single<PlitsoDatabase> {
        Room.databaseBuilder(
            get(),
            PlitsoDatabase::class.java,
            PlitsoDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    single<SyncDataManager> { SyncDataManagerImpl(get()) }

    viewModel { PlitsoViewModel(get()) }

}