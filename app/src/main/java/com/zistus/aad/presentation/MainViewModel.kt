package com.zistus.aad.presentation

import androidx.lifecycle.ViewModel
import com.zistus.aad.data.network.MainRepo
import com.zistus.aad.utils.ResultState
import com.zistus.aad.utils.SingleLiveEvent
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainViewModel(
    private val mainRepo: MainRepo = MainRepo
) : ViewModel() {
    private val _dataState: SingleLiveEvent<ResultState<MainViewState>> = SingleLiveEvent()
    val dataState = _dataState

    private val _viewState: SingleLiveEvent<MainViewState> = SingleLiveEvent()
    val viewState get() = _viewState

    private val eventChannel = ConflatedBroadcastChannel<MainStateEvent>(MainStateEvent.Idle())

    private fun processActions(stateEvent: MainStateEvent): Flow<ResultState<MainViewState>> {
        return when (stateEvent) {
            is MainStateEvent.GetCard -> {
                flow { emit(ResultState.data(MainViewState())) }
            }
            is MainStateEvent.Idle -> {
                flow { emit(ResultState.data(MainViewState())) }
            }
        }
    }
    
}