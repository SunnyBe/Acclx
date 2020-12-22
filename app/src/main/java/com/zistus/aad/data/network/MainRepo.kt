package com.zistus.aad.data.network

import com.zistus.aad.presentation.MainViewState
import com.zistus.aad.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface MainRepo {
    fun card(cardNumber: String?): Flow<ResultState<MainViewState>>
}