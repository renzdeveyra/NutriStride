package com.example.nutristride.auth

import com.example.nutristride.data.model.UserProfile
import com.example.nutristride.data.repository.FirestoreRepository
import com.example.nutristride.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthManager @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val userRepository: UserRepository
) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    val isUserLoggedIn: Boolean
        get() = auth.currentUser != null

    val currentUserId: String?
        get() = auth.currentUser?.uid ?: "test_user_id" // Provide a default user ID for testing

    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user?.let { user ->
                Result.success(user)
            } ?: Result.failure(Exception("Authentication failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, password: String, name: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.let { user ->
                // Create user profile in Firestore
                val userProfile = UserProfile(
                    userId = user.uid,
                    name = name,
                    email = email
                )

                // Save to Firestore
                firestoreRepository.saveUserProfile(userProfile)

                // Save to local database
                userRepository.insertUserProfile(userProfile)

                Result.success(user)
            } ?: Result.failure(Exception("User creation failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            auth.currentUser?.updatePassword(newPassword)?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateEmail(newEmail: String): Result<Unit> {
        return try {
            auth.currentUser?.updateEmail(newEmail)?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAccount(): Result<Unit> {
        return try {
            auth.currentUser?.delete()?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
