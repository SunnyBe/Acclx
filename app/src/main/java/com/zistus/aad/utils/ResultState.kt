package com.zistus.aad.utils

data class ResultState<T>(
    val data: T? = null,
    val error: Throwable? = null,
    val loading: Boolean? = false,
) {
    companion object {
        fun <T> loading() = ResultState<T>(
            data = null,
            error = null,
            loading = true,
        )

        fun <T> error(error: Throwable?) = ResultState<T>(
            data = null,
            error = error,
            loading = false,
        )

        fun <T> data(data: T?) = ResultState<T>(
            data = data,
            error = null,
            loading = false,
        )
    }
}