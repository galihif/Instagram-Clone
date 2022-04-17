package com.giftech.instagramclone.core.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.giftech.instagramclone.core.data.model.Post
import com.giftech.instagramclone.core.data.source.remote.network.ApiService
import com.giftech.instagramclone.core.utils.Mapper

class PostPagingSource(private val apiService: ApiService,private val token:String) : PagingSource<Int, Post>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, Post> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllPost(position, token)
            val listRes = responseData.listStory
            val listPost = Mapper.listStoryItemToListPost(listRes)
            LoadResult.Page(
                data = listPost,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (listPost.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}