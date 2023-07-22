package com.example.bookappretrofit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BooksViewModel () : ViewModel() {

    val bookListLiveData: MutableLiveData<List<BookItem>> = MutableLiveData()
    val repository = NetworkRepository.invoke(RetrofitInstance.apiClient)

    init {
        getBooks()
    }

    fun getBooks() {
        Log.d("rest_client","getBooks called in viewModel")
        viewModelScope.launch {
            val response = repository.getBooks()
            when (response) {
                is ApiResult.Success -> bookListLiveData.value = response.data
                is ApiResult.Unauthorized -> {}
                is ApiResult.Exception -> {}
                is ApiResult.Error -> {}
                is ApiResult.OtherException->{}
            }
        }
    }

    fun removeBook(position: Int) {
        val bookList = bookListLiveData.value.orEmpty().toMutableList()
        if (position in 0 until bookList.size) {
            bookList.removeAt(position)
            bookListLiveData.value = bookList
        }
    }
}