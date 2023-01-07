package com.ibrahimethemsen.firebasecallbackflow.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ibrahimethemsen.firebasecallbackflow.common.FirebaseResult
import com.ibrahimethemsen.firebasecallbackflow.common.getList
import com.ibrahimethemsen.firebasecallbackflow.model.firebasemodel.QuotesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestoreDb: FirebaseFirestore
) : ViewModel() {

    private var _uiStateLiveData = MutableLiveData<QuoteUiState>()
    val uiStateLiveData: LiveData<QuoteUiState> = _uiStateLiveData

    fun getQuotes() {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreDb.collection(QUOTES_COLLECTION).getList(::quoteMapper).onEach {
                handleResponse(it)
            }.catch { e ->
                _uiStateLiveData.postValue(
                    QuoteUiState(
                        errorMessage = e.message
                    )
                )
            }.launchIn(this)
        }
    }

    private fun handleResponse(response: FirebaseResult<List<QuotesModel>>) {
        when (response) {
            is FirebaseResult.Failed -> {
                _uiStateLiveData.postValue(
                    QuoteUiState().copy(
                        errorMessage = response.error
                    )
                )
            }
            FirebaseResult.Loading -> {
                _uiStateLiveData.postValue(
                    QuoteUiState().copy(
                        isLoading = true,
                    )
                )
            }
            is FirebaseResult.Success -> {
                _uiStateLiveData.postValue(
                    QuoteUiState().copy(
                        isLoading = false,
                        errorMessage = null,
                        data = response.data
                    )
                )
            }
        }
    }

    private fun getQuotesList(): Flow<FirebaseResult<List<QuotesModel>>> = callbackFlow {
        trySend(FirebaseResult.Loading)

        firestoreDb.collection(QUOTES_COLLECTION).get().addOnSuccessListener { result ->
            val quoteList = mutableListOf<QuotesModel>()
            result.forEach { document ->
                val name = document.get("name") as String
                val content = document.get("content") as String
                val quotation = document.get("quotation") as String
                val writer = document.get("writer") as String
                val year = document.get("year") as String
                quoteList.add(QuotesModel(name, content, quotation, writer, year))
            }.also {
                trySend(FirebaseResult.Success(quoteList))
            }
        }.addOnFailureListener {
            trySend(FirebaseResult.Failed(it.message))
        }
        awaitClose()
    }

    private fun quoteMapper(document: QueryDocumentSnapshot): QuotesModel {
        val name = document.get("name") as String
        val content = document.get("content") as String
        val quotation = document.get("quotation") as String
        val writer = document.get("writer") as String
        val year = document.get("year") as String
        return QuotesModel(name, content, quotation, writer, year)
    }

    companion object {
        const val QUOTES_COLLECTION = "quotes"
    }
}

data class QuoteUiState(
    val data: List<QuotesModel> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)