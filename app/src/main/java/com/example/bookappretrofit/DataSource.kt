package com.example.bookappretrofit

interface DataSource {

    suspend fun getBooks(): ApiResult<List<BookItem>>

}