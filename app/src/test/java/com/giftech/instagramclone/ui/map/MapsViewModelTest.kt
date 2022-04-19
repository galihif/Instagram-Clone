package com.giftech.instagramclone.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.giftech.instagramclone.DataDummy
import com.giftech.instagramclone.core.data.MainRepository
import com.giftech.instagramclone.core.data.Result
import com.giftech.instagramclone.core.data.model.Post
import com.giftech.instagramclone.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mainRepository: MainRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyPost = DataDummy.generateDummyListPostLocation()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(mainRepository)
    }

    @Test
    fun `when Get Post With Location Should Not Null and Return Same Result`(){
        val expectedPost = MutableLiveData<Result<List<Post>>>()
        expectedPost.value = Result.Success(dummyPost)
        `when`(mapsViewModel.getPostWithLocation()).thenReturn(expectedPost)

        val actualPost = mapsViewModel.getPostWithLocation().getOrAwaitValue()
        Mockito.verify(mainRepository).getPostWithLocation()
        Assert.assertNotNull(actualPost)
        Assert.assertEquals(dummyPost.size, (actualPost as Result.Success).data.size)
    }
}