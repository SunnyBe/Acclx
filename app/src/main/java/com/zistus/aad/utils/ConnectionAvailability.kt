package com.zistus.aad.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

object ConnectionAvailability {
    private val classTag = javaClass.simpleName

    fun hasNetworkAvailable(context: Context): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = context.getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        Log.d(classTag, "hasNetworkAvailable: ${(network != null)}")
        return (network?.isConnected) ?: false
    }
}
