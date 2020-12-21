package com.zistus.aad.presentation

sealed class MainStateEvent {
    data class GetCard(val cardNumber: String?) : MainStateEvent()
    class Idle() : MainStateEvent()
}
