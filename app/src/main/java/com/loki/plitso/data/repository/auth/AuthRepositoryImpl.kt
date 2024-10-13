package com.loki.plitso.data.repository.auth

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.perf.trace
import com.loki.plitso.data.remote.mealdb.models.User
import com.loki.plitso.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.io.IOException


class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager
) : AuthRepository {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                this.trySend(auth.currentUser?.let {
                    User(
                        id = it.uid,
                        email = it.email!!
                    )
                } ?: User())
            }

            auth.addAuthStateListener(listener)
            awaitClose {
                auth.removeAuthStateListener(listener)
            }
        }

    override fun authenticate(token: String): Flow<Resource<User>> = flow {
        trace(LOGIN_USER_TRACE) {
            try {
                emit(Resource.Loading())
                val credential = GoogleAuthProvider.getCredential(token, null)
                val user = auth.signInWithCredential(credential).await().user
                emit(
                    Resource.Success(
                        User(
                            id = user!!.uid,
                            email = user.email!!,
                            username = user.displayName,
                            imageUrl = user.photoUrl!!
                        )
                    )
                )

            } catch (e: FirebaseException) {
                emit(Resource.Error(e.localizedMessage ?: "Something went wrong!"))
            } catch (e: IOException) {
                emit(Resource.Error("No Network Connection!"))
            }
        }
    }

    override fun logOut(): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            emit(Resource.Success("logout successful"))
        } catch (e: FirebaseException) {
            emit(Resource.Error(e.localizedMessage ?: "Something went wrong!"))
        } catch (e: IOException) {
            emit(Resource.Error("No Network Connection!"))
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        //traces
        const val LOGIN_USER_TRACE = "login_trace"
    }
}