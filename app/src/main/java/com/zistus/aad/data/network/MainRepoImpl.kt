package com.zistus.aad.data.network

import com.zistus.aad.presentation.MainViewState
import com.zistus.aad.utils.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class MainRepoImpl constructor(private val network: ApiService = RetrofitBuilder.network()) :
    MainRepo {
    override fun card(cardNumber: String?): Flow<ResultState<MainViewState>> {
        return flow {
            val card = cardNumber?.let {
                network.card(cardNumber)
            }
            emit(ResultState.data(MainViewState(card = card)))
        }.onStart {
            emit(ResultState.loading())
        }.catch {
            emit(ResultState.error(it.cause))
        }
            .flowOn(Dispatchers.IO)
    }

}