package com.example.jetpackcomposechatapp.di

import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun providesFireBAseFireStore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun providesFireBaseStorage(): FirebaseStorage = Firebase.storage

    @Provides
    @Singleton
    fun providesFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

}