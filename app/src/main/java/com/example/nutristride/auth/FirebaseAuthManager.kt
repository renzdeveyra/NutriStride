package com.example.nutristride.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import com.google.firebase.auth.GoogleAuthProvider

@Singleton
class FirebaseAuthManager @Inject constructor(
    private val auth: FirebaseAuth
) {
    private val TAG = "FirebaseAuthManager"
    
    val currentUser: FirebaseUser?
        get() = auth.currentUser
    
    val isUserLoggedIn: Boolean
        get() = auth.currentUser != null
    
    val currentUserId: String?
        get() = auth.currentUser?.uid
    
    suspend fun getCurrentUserId(): String? {
        return currentUserId
    }
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                Log.d(TAG, "User signed in: ${it.uid}")
                Result.success(it)
            } ?: Result.failure(Exception("Authentication failed"))
        } catch (e: Exception) {
            Log.e(TAG, "Sign in failed: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                Log.d(TAG, "User created: ${it.uid}")
                Result.success(it)
            } ?: Result.failure(Exception("User creation failed"))
        } catch (e: Exception) {
            Log.e(TAG, "User creation failed: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    fun signOut() {
        auth.signOut()
        Log.d(TAG, "User signed out")
    }
    
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Log.d(TAG, "Password reset email sent to $email")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send password reset email: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    suspend fun deleteAccount(): Result<Unit> {
        val user = auth.currentUser ?: return Result.failure(Exception("No user logged in"))
        
        return try {
            user.delete().await()
            Log.d(TAG, "User account deleted")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete account: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            authResult.user?.let {
                Log.d(TAG, "User signed in with Google: ${it.uid}")
                Result.success(it)
            } ?: Result.failure(Exception("Google authentication failed"))
        } catch (e: Exception) {
            Log.e(TAG, "Google sign in failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun signInAnonymously(): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInAnonymously().await()
            authResult.user?.let {
                Log.d(TAG, "User signed in anonymously: ${it.uid}")
                Result.success(it)
            } ?: Result.failure(Exception("Anonymous authentication failed"))
        } catch (e: Exception) {
            Log.e(TAG, "Anonymous sign in failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    fun isAnonymousUser(): Boolean {
        return auth.currentUser?.isAnonymous ?: false
    }
}
