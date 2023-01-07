package com.ibrahimethemsen.firebasecallbackflow.common

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


fun <T : Any> CollectionReference.getList(
    mapper : (QueryDocumentSnapshot) -> T
) : Flow<FirebaseResult<List<T>>> = callbackFlow {
    get().addOnSuccessListener { result ->
        val list = mutableListOf<T>()
        result.forEach { document ->
            list.add(mapper.invoke(document))
        }.also {
            trySend(FirebaseResult.Success(list))
        }
    }.addOnFailureListener {
        trySend(FirebaseResult.Failed(it.message))
    }
    awaitClose()
}


