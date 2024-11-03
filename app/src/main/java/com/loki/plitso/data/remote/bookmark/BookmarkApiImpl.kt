package com.loki.plitso.data.remote.bookmark

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val BOOKMARKS_FIELD = "bookmarks"

class BookmarkApiImpl(userId: String): BookmarkApi {

    private val bookmarkRef = Firebase.firestore.document("users/$userId")

    override val bookmarks: Flow<List<String>>
        get() {
            return callbackFlow {
                val listenerRegistration = bookmarkRef.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val recipeIds = snapshot.get(BOOKMARKS_FIELD) as? List<String> ?: emptyList()
                        trySend(recipeIds)
                    }
                }
                awaitClose { listenerRegistration.remove() }
            }
        }


    override suspend fun saveBookmark(recipeId: String): Boolean {
        return suspendCoroutine {continuation ->
            bookmarkRef.update(BOOKMARKS_FIELD, FieldValue.arrayUnion(recipeId))
                .addOnSuccessListener {
                    continuation.resume(true)
                }.addOnFailureListener {
                    continuation.resume(false)
                }
        }
    }

    override suspend fun deleteBookmark(recipeId: String) {
        return suspendCoroutine { continuation ->
            bookmarkRef.update(BOOKMARKS_FIELD, FieldValue.arrayRemove(recipeId))
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun isBookmarked(recipeId: String): Boolean {
        return suspendCoroutine { continuation ->
            bookmarkRef.get()
                .addOnSuccessListener {document ->
                    val bookmarks = document.get(BOOKMARKS_FIELD) as List<*>
                    continuation.resume(bookmarks.contains(recipeId))
                }.addOnFailureListener { _ ->
                    continuation.resume(false)
                }
        }
    }
}
