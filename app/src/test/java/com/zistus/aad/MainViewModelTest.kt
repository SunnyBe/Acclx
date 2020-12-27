package com.zistus.aad

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zistus.aad.data.network.MainRepo
import com.zistus.aad.fakes.NetworkFakes
import com.zistus.aad.presentation.MainViewModel
import com.zistus.aad.presentation.MainViewState
import com.zistus.aad.utils.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*

class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Mock
    lateinit var mainRepo: MainRepo

    @Captor
    private lateinit var captor: ArgumentCaptor<MainViewState>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mainViewModel = MainViewModel(mainRepo)
    }

    @Test
    fun fetchCard_SuccessUpdatesViewStateAndDataState() {
        coroutineScope.runBlockingTest {
            val testFlow = flow {
                emit(ResultState.data(MainViewState(card = NetworkFakes.testCardQuery("012345678912345"))))
            }
            mainViewModel.queryCard("012345678912345")
            Mockito.`when`(mainRepo.card("012345678912345")).thenReturn(testFlow)

            val expectedType = "debit"
            Assert.assertEquals(expectedType, mainViewModel.dataState.getValueForTest()?.data?.getContentIfNotHandled()?.card?.type)
        }
    }

    @Test
    fun fetchCard_ErrorUpdatesViewStateAndDataState() {
        coroutineScope.runBlockingTest {
            val testFlow = flow {
                emit(ResultState.error<MainViewState>(Throwable("Test Error")))
            }
            mainViewModel.queryCard("012345678912345")
            Mockito.`when`(mainRepo.card("012345678912345")).thenReturn(testFlow)

            val expectedType = "Test Error"
            Assert.assertEquals(expectedType, mainViewModel.dataState.getValueForTest()?.error?.message)
        }
    }

    @Test
    fun fetchCard_LoadingUpdatesViewStateAndDataState() {
        coroutineScope.runBlockingTest {
            val testFlow = flow {
                emit(ResultState.loading<MainViewState>())
            }
            mainViewModel.queryCard("012345678912345")
            Mockito.`when`(mainRepo.card("012345678912345")).thenReturn(testFlow)

            val expectedType = true
            Assert.assertEquals(expectedType, mainViewModel.dataState.getValueForTest()?.loading)
        }
    }
}
