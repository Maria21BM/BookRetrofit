package com.example.bookappretrofit

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitInstance {

    companion object {
        val BASE_URL = "https://www.jsonkeeper.com/"

        val apiClient: BookService by lazy {
            Log.d("rest_client", "lazy initialization")
            val mHttpLoggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val mOkHttpClient =
                OkHttpClient.Builder().addInterceptor(mHttpLoggingInterceptor).build()


            val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(mOkHttpClient).build()
            return@lazy retrofit.create()
        }
    }

}