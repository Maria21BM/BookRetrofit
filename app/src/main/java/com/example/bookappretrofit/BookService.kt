package com.example.bookappretrofit

import retrofit2.Response
import retrofit2.http.GET

interface BookService {

    @GET("/b/UP1D")
    suspend fun getBooks() : Response<BookList>

}