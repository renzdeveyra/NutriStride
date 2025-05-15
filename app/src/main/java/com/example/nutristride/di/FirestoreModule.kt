package com.example.nutristride.di

import com.example.nutristride.data.repository.FirestoreRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {
    
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
    
    @Provides
    @Singleton
    fun provideFirestoreRepository(): FirestoreRepository {
        return FirestoreRepository()
    }
}
