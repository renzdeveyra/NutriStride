package com.example.nutristride.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutristride.auth.FirebaseAuthManager
import com.example.nutristride.data.sync.SyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: FirebaseAuthManager,
    private val syncManager: SyncManager
) : ViewModel() {
    
    private val TAG = "AuthViewModel"
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState
    
    init {
        checkAuthState()
    }
    
    private fun checkAuthState() {
        _authState.value = if (authManager.isUserLoggedIn) {
            if (authManager.isAnonymousUser()) {
                AuthState.GuestUser
            } else {
                AuthState.Authenticated
            }
        } else {
            AuthState.Unauthenticated
        }
    }
    
    fun signInWithEmailAndPassword(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            authManager.signInWithEmailAndPassword(email, password)
                .onSuccess {
                    _authState.value = AuthState.Authenticated
                    syncManager.schedulePeriodicalSync()
                }
                .onFailure { e ->
                    _authState.value = AuthState.Error(e.message ?: "Authentication failed")
                }
        }
    }
    
    fun signInWithGoogle(idToken: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            authManager.signInWithGoogle(idToken)
                .onSuccess {
                    _authState.value = AuthState.Authenticated
                    syncManager.schedulePeriodicalSync()
                }
                .onFailure { e ->
                    _authState.value = AuthState.Error(e.message ?: "Google authentication failed")
                }
        }
    }
    
    fun signInAsGuest() {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            authManager.signInAnonymously()
                .onSuccess {
                    _authState.value = AuthState.GuestUser
                }
                .onFailure { e ->
                    _authState.value = AuthState.Error(e.message ?: "Guest sign-in failed")
                }
        }
    }
    
    fun signOut() {
        authManager.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    object GuestUser : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}