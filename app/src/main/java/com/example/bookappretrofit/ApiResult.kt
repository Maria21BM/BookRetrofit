package com.example.bookappretrofit

sealed class ApiResult<T : Any> {
    class Success<T : Any>(val data: T) : ApiResult<T>()
    class Error<T : Any>(
        val code: Int?, val message: String?, val parsedError: String?
    ) : ApiResult<T>()

    class Unauthorized<T : Any> : ApiResult<T>()
    class Exception<T : Any>(
        val message: String?
    ) : ApiResult<T>()
    class OtherException<T:Any>():ApiResult<T>()
}