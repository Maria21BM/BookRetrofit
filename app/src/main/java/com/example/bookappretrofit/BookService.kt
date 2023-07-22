package com.example.bookappretrofit

import retrofit2.Response
import retrofit2.http.GET

interface BookService {

    @GET("/b/VUYS")
    suspend fun getBooks() : Response<List<BookItem>>

}