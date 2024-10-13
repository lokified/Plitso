package com.loki.plitso.di

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.loki.plitso.BuildConfig
import com.loki.plitso.data.local.PlitsoDatabase
import com.loki.plitso.data.local.dao.AiAnswerDao
import com.loki.plitso.presentation.ai.AiData
import com.loki.plitso.presentation.ai.AiViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val aiModule = module {
    val config = generationConfig {
        temperature = 0.7f
    }

    single {
        GenerativeModel(
            modelName = "gemini-1.5-flash-latest",
            apiKey = BuildConfig.geminiApiKey,
            generationConfig = config
        )
    }

    single<AiAnswerDao> { get<PlitsoDatabase>().aiAnswerDao }
    single { AiData(get(), get()) }
    viewModel { AiViewModel(get(), get(), get(), get()) }
}