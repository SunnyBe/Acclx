package com.zistus.aad

import com.zistus.aad.presentation.MainViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(com.zistus.aad.data.network.MainRepo)
    }

    @Test
    fun fetchCard_updatesViewStateAndDataState() {

    }
}