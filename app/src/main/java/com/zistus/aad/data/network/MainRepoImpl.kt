package com.zistus.aad.data.network

import android.content.Context
import com.zistus.aad.presentation.MainViewState
import com.zistus.aad.utils.ConnectionAvailability
import com.zistus.aad.utils.ResultState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*

class MainRepoImpl constructor(
    private val context: Context,
    private val network: ApiService = RetrofitBuilder.network()
) :
    MainRepo {
    override fun card(cardNumber: String?): Flow<ResultState<MainViewState>> {
        return flow<ResultState<MainViewState>> {
            val card = cardNumber?.let {
                network.card(cardNumber)
            }
            emit(ResultState.data(MainViewState(card = card)))
        }.onStart {
            if (isInternetAvailable(context)) {
                emit(ResultState.loading())
            }
        }.catch {
            it.printStackTrace()
            emit(ResultState.error(it))
        }
            .flowOn(IO)
    }

    // Check if mobile phone has internet connectivity turned on
    private fun isInternetAvailable(context: Context): Boolean {
        if (ConnectionAvailability.hasNetworkAvailable(context)) {
            return true
        } else throw RuntimeException("Connect to internet and try again!")
    }
}
