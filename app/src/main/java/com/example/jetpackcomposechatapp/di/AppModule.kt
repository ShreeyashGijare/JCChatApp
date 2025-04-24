package com.example.jetpackcomposechatapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideFireBaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun providesFireBAseFireStore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun providesFireBaseStorage(): FirebaseStorage = Firebase.storage

    @Provides
    fun providesFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

}