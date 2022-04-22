package com.giftech.instagramclone.core.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.giftech.instagramclone.DataDummy
import com.giftech.instagramclone.MainCoroutineRule
import com.giftech.instagramclone.core.adapter.PostAdapter
import com.giftech.instagramclone.core.data.Result
import com.giftech.instagramclone.core.data.model.Post
import com.giftech.instagramclone.core.data.source.local.LocalDataSource
import com.giftech.instagramclone.core.data.source.remote.RemoteDataSource
import com.giftech.instagramclone.getOrAwaitValue
import com.giftech.instagramclone.ui.home.PagedTestDataSources
import com.giftech.instagramclone.ui.home.noopListUpdateCallback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    private lateinit var local:LocalDataSource
    private lateinit var remote:RemoteDataSource
    private lateinit var mainRepository:FakeMainRepository

    @Before
    fun setUp(){
        local = Mockito.mock(LocalDataSource::class.java)
        remote = Mockito.mock(RemoteDataSource::class.java)
        mainRepository = FakeMainRepository(local,remote)
    }

    @Test
    fun getAllPost() = mainCoroutineRules.runBlockingTest {
        val dummyPost = DataDummy.generateDummyListPost()
        val data = PagedTestDataSources.snapshot(dummyPost)
        val post = MutableLiveData<PagingData<Post>>()
        post.value = data
        Mockito.`when`(remote.getAllPost("token")).thenReturn(post)
        val actualPost = remote.getAllPost("token").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = PostAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )
        differ.submitData(actualPost)

        advanceUntilIdle()
        Mockito.verify(remote).getAllPost("token")
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyPost.size, differ.snapshot().size)
        Assert.assertEquals(dummyPost[0].id, differ.snapshot()[0]?.id)
    }

    @Test
    fun getPostWithLocation() = mainCoroutineRules.runBlockingTest{
        val dummyPost = DataDummy.generateDummyListPostLocation()
        val expectedPost = MutableLiveData<Result<List<Post>>>()
        expectedPost.value = Result.Success(dummyPost)
        Mockito.`when`(remote.getPostWithLocation("token")).thenReturn(expectedPost)

        val actualPost = remote.getPostWithLocation("token").getOrAwaitValue()

        Mockito.verify(remote).getPostWithLocation("token")
        Assert.assertNotNull(actualPost)
        Assert.assertTrue(actualPost is Result.Success)
        Assert.assertEquals(dummyPost.size, (actualPost as Result.Success).data.size)
    }
}