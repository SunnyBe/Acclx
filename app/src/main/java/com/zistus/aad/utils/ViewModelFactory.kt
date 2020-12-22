package com.zistus.aad.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zistus.aad.data.network.MainRepoImpl
import com.zistus.aad.presentation.MainViewModel

class ViewModelFactory constructor(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        // If model class is correct return them as ViewModel with Value
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepoImpl(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}