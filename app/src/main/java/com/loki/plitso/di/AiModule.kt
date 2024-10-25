package com.loki.plitso.di

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.loki.plitso.BuildConfig
import com.loki.plitso.data.local.PlitsoDatabase
import com.loki.plitso.data.local.dao.AiAnswerDao
import com.loki.plitso.data.local.dao.ChatHistoryDao
import com.loki.plitso.presentation.ai.AiData
import com.loki.plitso.presentation.ai.AiViewModel
import com.loki.plitso.presentation.ai.PromptUtil
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val aiModule =
    module {
        val config =
            generationConfig {
                temperature = 0.7f
            }

        val systemInstructions =
            content {
                text(PromptUtil.OUT_OF_CONTEXT_WARNING)
            }

        single {
            GenerativeModel(
                modelName = "gemini-1.5-flash-latest",
                apiKey = BuildConfig.geminiApiKey,
                generationConfig = config,
                systemInstruction = systemInstructions,
            )
        }

        single<AiAnswerDao> { get<PlitsoDatabase>().aiAnswerDao }
        single<ChatHistoryDao> { get<PlitsoDatabase>().chatHistoryDao }
        single { AiData(get(), get()) }
        viewModel { AiViewModel(get(), get(), get(), get()) }
    }
