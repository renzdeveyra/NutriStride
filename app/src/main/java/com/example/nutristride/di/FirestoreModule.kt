package com.example.nutristride.di

import com.example.nutristride.data.repository.FirestoreRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        return Firebase.firestore
    }
    
    @Provides
    @Singleton
    fun provideFirestoreRepository(firestore: FirebaseFirestore): FirestoreRepository {
        return FirestoreRepository(firestore)
    }
}
