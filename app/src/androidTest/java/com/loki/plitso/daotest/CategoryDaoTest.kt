package com.loki.plitso.daotest

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.loki.plitso.data.local.dao.CategoryDao
import com.loki.plitso.data.local.models.Category
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryDaoTest: BaseDbTest() {

    private lateinit var categoryDao: CategoryDao

    @Before
    fun setUp() {
        categoryDao = db.categoryDao
    }


    @Test
    fun insert_and_get_categories() = runTest {
        val categories = listOf(
            Category("12345", "chicken", "https://test.image"),
            Category("12335", "pork", "https://test.image"),
        )

        categoryDao.insert(categories)

        val dbCategories = categoryDao.getCategories().first()

        Truth.assertThat(dbCategories.size).isEqualTo(2)
        Truth.assertThat(dbCategories[0].title).isEqualTo("chicken")
    }
}
