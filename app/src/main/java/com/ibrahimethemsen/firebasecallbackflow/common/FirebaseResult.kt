package com.ibrahimethemsen.firebasecallbackflow.common


sealed class FirebaseResult<out T : Any>{
    object Loading : FirebaseResult<Nothing>()
    data class Success<out T : Any>(val data : T) : FirebaseResult<T>()
    data class Failed<out T : Any>(val error : String?) : FirebaseResult<T>()
}
