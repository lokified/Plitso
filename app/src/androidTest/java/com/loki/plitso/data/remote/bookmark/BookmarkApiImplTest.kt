package com.loki.plitso.data.remote.bookmark

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.coroutines.resume

@RunWith(AndroidJUnit4::class)
class BookmarkApiImplTest{

    private val user = "testUser"
    private val bookmarkApi = BookmarkApiImpl(user)

    @Test
    fun addBookmarkNoExistingDocument() = runBlocking{
        val expected = "recipe1"
        bookmarkApi.saveBookmark(expected)
        val actual = bookmarkApi.bookmarks.first().first()
        assertEquals(expected, actual)
    }

    @Test
    fun addBookmarkOnExistingDocumentWithoutBookmarkField() = runBlocking{
        suspendCancellableCoroutine { c ->
            Firebase.firestore.document("users/$user")
                .set("name" to "John Doe")
                .addOnCompleteListener {
                    c.resume(Unit)
                }
        }
        val expected = "recipe1"
        bookmarkApi.saveBookmark(expected)
        val actual = bookmarkApi.bookmarks.first().first()
        assertEquals(expected, actual)
    }

    @Test
    fun addBookmarkOnExistingDocumentWithBookmarkField() = runBlocking{
        suspendCancellableCoroutine { c ->
            Firebase.firestore.document("users/$user")
                .set(
                    mapOf(
                        "name" to "John Doe",
                        BOOKMARKS_FIELD to listOf("recipe12")
                    )
                )
                .addOnCompleteListener {
                    c.resume(Unit)
                }
        }
        val expected = "recipe1"
        bookmarkApi.saveBookmark(expected)
        val bookmarks = bookmarkApi.bookmarks.first()
        assertEquals(2, bookmarks.size)
        assert(bookmarks.contains(expected))
    }

    @Test
    fun addSameBookmarkTwice() = runBlocking{
        suspendCancellableCoroutine { c ->
            Firebase.firestore.document("users/$user")
                .set(
                    mapOf(
                        "name" to "John Doe",
                        BOOKMARKS_FIELD to listOf("recipe12")
                    )
                )
                .addOnCompleteListener {
                    c.resume(Unit)
                }
        }
        val expected = "recipe12"
        bookmarkApi.saveBookmark(expected)
        val bookmarks = bookmarkApi.bookmarks.first()
        assertEquals(1, bookmarks.size)
        assertEquals(1, bookmarks.count{it == expected})
    }

    @After
    fun cleanUp() = runBlocking{
        suspendCancellableCoroutine<Unit> { continuation ->
            Firebase.firestore.document("users/$user").delete().addOnCompleteListener {
                continuation.resume(Unit)
            }

        }
    }
}
