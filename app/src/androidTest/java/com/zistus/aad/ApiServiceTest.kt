package com.zistus.aad

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zistus.aad.utils.MainCoroutineScopeRule
import com.zistus.aad.utils.NetworkServiceCompletableFake
import com.zistus.aad.utils.NetworkServiceFake
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ApiServiceTest {

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    lateinit var apiServiceCompletableFake: NetworkServiceCompletableFake
    lateinit var networkServiceFake: NetworkServiceFake

    @Before
    fun setUp() {
        apiServiceCompletableFake = NetworkServiceCompletableFake()
        networkServiceFake = NetworkServiceFake()
    }

    @Test
    fun queryCard_returnSuccessfulCardResponse() {
    }
}
