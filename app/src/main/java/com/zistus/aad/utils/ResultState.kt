package com.zistus.aad.utils

data class ResultState<T>(
    val data: T? = null,
    val error: Throwable? = null,
    val loading: Boolean? = false,
    val loadingMsg: String? = "Please wait"
) {
    companion object {
        fun <T> loading(msg: String?) = ResultState<T>(
            data = null,
            error = null,
            loading = true,
            loadingMsg = msg
        )

        fun <T> error(error: Throwable?) = ResultState<T>(
            data = null,
            error = error,
            loading = false,
            loadingMsg = null
        )

        fun <T> data(data: T?) = ResultState<T>(
            data = data,
            error = null,
            loading = false,
            loadingMsg = null
        )
    }
}