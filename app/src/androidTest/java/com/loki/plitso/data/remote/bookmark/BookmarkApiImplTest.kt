package com.loki.plitso.data.remote.bookmark

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.time.withTimeoutOrNull
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.coroutines.resume

@RunWith(AndroidJUnit4::class)
class BookmarkApiImplTest{

    private val user = "testUser"
    private val bookmarkApi = BookmarkApiImpl(user)

    @Test
    fun getBookmarkNoExistingDocument() = runBlocking{
        val bookmarks = withTimeoutOrNull(5000){ bookmarkApi.bookmarks.first()}
        assertEquals(null, bookmarks)
    }

    @Test
    fun getBookmarkExistingDocumentNoBookmarkField() = runBlocking{
        suspendCancellableCoroutine { c ->
            Firebase.firestore.document("users/$user")
                .set("name" to "John Doe")
                .addOnCompleteListener {
                    c.resume(Unit)
                }
        }
        val bookmarks = bookmarkApi.bookmarks.first()
        assertEquals(0, bookmarks.size)
    }

    @Test
    fun getBookmarkExistingDocumentWithEmptyBookmarkField() = runBlocking{
        suspendCancellableCoroutine { c ->
            Firebase.firestore.document("users/$user")
                .set(
                    mapOf(
                        "name" to "John Doe",
                        BOOKMARKS_FIELD to emptyList<String>()
                    )
                )
                .addOnCompleteListener {
                    c.resume(Unit)
                }
        }
        val bookmarks = bookmarkApi.bookmarks.first()
        assertEquals(0, bookmarks.size)
    }

    @Test
    fun getBookmarkExistingDocumentWithPrepopulatedBookmarkField() = runBlocking{
        suspendCancellableCoroutine { c ->
            Firebase.firestore.document("users/$user")
                .set(
                    mapOf(
                        "name" to "John Doe",
                        BOOKMARKS_FIELD to listOf("1", "2")
                    )
                ).addOnCompleteListener {
                    c.resume(Unit)
                }
        }
        val bookmarks = bookmarkApi.bookmarks.first()
        assertEquals(2, bookmarks.size)
    }

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


    @Test
    fun deleteBookmarkFromNonExistingDocument() = runBlocking{
        val recipeId = "recipe12"
        val exception = runCatching { bookmarkApi.deleteBookmark(recipeId)}.exceptionOrNull()
        assert(exception is FirebaseFirestoreException)
    }

    @Test
    fun deleteBookmarkFromExistingDocumentWithNonExistingBookmarkField() = runBlocking{
        suspendCancellableCoroutine { c ->
            Firebase.firestore.document("users/$user")
                .set(
                    mapOf(
                        "name" to "John Doe",
                    )
                )
                .addOnCompleteListener {
                    c.resume(Unit)
                }
        }
        val recipeId = "recipe12"
        bookmarkApi.deleteBookmark(recipeId)
        val bookmarks = bookmarkApi.bookmarks.first()
        assertEquals(0, bookmarks.size)
    }

    @Test
    fun deleteBookmarkWithExistingField() = runBlocking{
        suspendCancellableCoroutine { c ->
            Firebase.firestore.document("users/$user")
                .set(
                    mapOf(
                        "name" to "John Doe",
                        BOOKMARKS_FIELD to listOf("recipe12", "rec2", "eiwe2")
                    )
                )
                .addOnCompleteListener {
                    c.resume(Unit)
                }
        }
        val recipeId = "recipe12"
        bookmarkApi.deleteBookmark(recipeId)
        val bookmarks = bookmarkApi.bookmarks.first()
        assertEquals(2, bookmarks.size)
        assertFalse(bookmarks.contains(recipeId))
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
