package com.zistus.aad.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zistus.aad.data.network.MainRepo
import com.zistus.aad.data.network.MainRepoImpl
import com.zistus.aad.utils.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

class MainViewModel(
    private val mainRepo: MainRepo = MainRepoImpl()
) : ViewModel() {

    private val stateChannel = ConflatedBroadcastChannel<MainViewState>()
    private val eventChannel = ConflatedBroadcastChannel<MainStateEvent>(MainStateEvent.Idle())

    val dataState = eventChannel.asFlow()
        .flatMapLatest { stateEvent ->
            processActions(stateEvent)
        }
        .asLiveData()

    private fun processActions(stateEvent: MainStateEvent): Flow<ResultState<MainViewState>> {
        return when (stateEvent) {
            is MainStateEvent.GetCard -> {
                mainRepo.card(stateEvent.cardNumber)
            }
            is MainStateEvent.Idle -> {
                flow { emit(ResultState.data(MainViewState())) }
            }
        }
    }

    fun setViewState(viewState: MainViewState) {
        Log.i(javaClass.simpleName, "Setting view state: $viewState")
        stateChannel.offer(viewState)
    }

    fun queryCard(cardNumber: String) {
        eventChannel.offer(MainStateEvent.GetCard(cardNumber = cardNumber))
    }

}