package com.ibrahimethemsen.firebasecallbackflow.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@[Module InstallIn(ViewModelComponent::class)]
object FirebaseModule {
    @[Provides ViewModelScoped]
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}