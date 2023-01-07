package com.ibrahimethemsen.firebasecallbackflow.presentation.home

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestoreDb : FirebaseFirestore
): ViewModel() {

}