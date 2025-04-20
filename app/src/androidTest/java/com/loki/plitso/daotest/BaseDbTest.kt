package com.loki.plitso.daotest

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.loki.plitso.data.local.PlitsoDatabase
import com.loki.plitso.helper.MainCoroutineRule
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseDbTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = MainCoroutineRule()

    lateinit var db: PlitsoDatabase


    @Before
    open fun setUpDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            PlitsoDatabase::class.java,
        ).allowMainThreadQueries()
            .build()
    }

    @After
    open fun tearDownDb() {
        db.clearAllTables()
        db.close()
    }
}
