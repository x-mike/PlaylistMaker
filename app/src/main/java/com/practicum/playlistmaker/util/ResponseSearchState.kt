package com.practicum.playlistmaker.util

sealed class ResponseSearchState<T>(val data: T? = null, val statusError: Boolean? = false) {

    class Successful<T>(data:T?): ResponseSearchState<T>(data)

    class Error<T>(statusError:Boolean?): ResponseSearchState<T>(data = null,statusError)

}