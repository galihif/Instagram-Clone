package com.giftech.instagramclone.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.giftech.instagramclone.DataDummy
import com.giftech.instagramclone.MainCoroutineRule
import com.giftech.instagramclone.core.adapter.PostAdapter
import com.giftech.instagramclone.core.data.model.Post
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
class HomeViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()
    @Mock
    private lateinit var viewModel: HomeViewModel

    @Test
    fun `when Get Post Should Not Null`() = mainCoroutineRules.runBlockingTest {
        val dummyQuote = DataDummy.generateDummyListPost()
        val data = PagedTestDataSources.snapshot(dummyQuote)
        val post = MutableLiveData<PagingData<Post>>()
        post.value = data
        Mockito.`when`(viewModel.post).thenReturn(post)
        val actualPost = viewModel.post.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = PostAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )
        differ.submitData(actualPost)

        advanceUntilIdle()
        Mockito.verify(viewModel).post
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyQuote.size, differ.snapshot().size)
        Assert.assertEquals(dummyQuote[0].id, differ.snapshot()[0]?.id)

    }

    @Test
    fun `when getUser Should Not Null and Have Attributes`(){
        val dummyUser = DataDummy.generateDummyUser()
        Mockito.`when`(viewModel.getUser()).thenReturn(dummyUser)

        val actualUser = viewModel.getUser()
        Mockito.verify(viewModel).getUser()
        Assert.assertNotNull(actualUser)
        Assert.assertEquals(actualUser.username, dummyUser.username)
        Assert.assertEquals(actualUser.email, dummyUser.email)
        Assert.assertEquals(actualUser.password, dummyUser.password)
    }

    @Test
    fun `when logout` (){
        Mockito.doNothing().`when`(viewModel).logout()
        viewModel.logout()
        Mockito.verify(viewModel).logout()
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class PagedTestDataSources private constructor(private val items: List<Post>) :
    PagingSource<Int, LiveData<List<Post>>>() {
    companion object {
        fun snapshot(items: List<Post>): PagingData<Post> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Post>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Post>>> {
        return LoadResult.Page(emptyList(), 0 , 1)
    }
}