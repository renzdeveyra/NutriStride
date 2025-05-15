package com.example.nutristride.di

import com.example.nutristride.auth.FirebaseAuthManager
import com.example.nutristride.data.repository.FirestoreRepository
import com.example.nutristride.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    
    @Provides
    @Singleton
    fun provideFirebaseAuthManager(
        firestoreRepository: FirestoreRepository,
        userRepository: UserRepository
    ): FirebaseAuthManager {
        return FirebaseAuthManager(firestoreRepository, userRepository)
    }
}
