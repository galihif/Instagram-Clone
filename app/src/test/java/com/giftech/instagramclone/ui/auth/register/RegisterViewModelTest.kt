package com.giftech.instagramclone.ui.auth.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.giftech.instagramclone.DataDummy
import com.giftech.instagramclone.MainCoroutineRule
import com.giftech.instagramclone.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest{


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()
    @Mock
    private lateinit var viewModel: RegisterViewModel

    @Test
    fun `when register should return true and not null`() = mainCoroutineRules.runBlockingTest {
        val dummyResult = MutableLiveData<Boolean>(true)
        val dummyUser = DataDummy.generateDummyUser()
        Mockito.`when`(viewModel.register(dummyUser)).thenReturn(dummyResult)

        val actualResult = viewModel.register(dummyUser).getOrAwaitValue()
        Mockito.verify(viewModel).register(dummyUser)
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(dummyResult.value, actualResult)
    }

    @Test
    fun `when register should return false and not null`() = mainCoroutineRules.runBlockingTest {
        val dummyResult = MutableLiveData<Boolean>(false)
        val dummyUser = DataDummy.generateDummyUser()
        Mockito.`when`(viewModel.register(dummyUser)).thenReturn(dummyResult)

        val actualResult = viewModel.register(dummyUser).getOrAwaitValue()
        Mockito.verify(viewModel).register(dummyUser)
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(dummyResult.value, actualResult)
    }


}