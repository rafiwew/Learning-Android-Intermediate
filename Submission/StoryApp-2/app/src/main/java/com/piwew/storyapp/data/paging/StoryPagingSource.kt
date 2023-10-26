package com.piwew.storyapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.piwew.storyapp.data.api.response.ListStoryItem
import com.piwew.storyapp.data.api.retrofit.ApiConfig
import com.piwew.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class StoryPagingSource(
    private val userPreference: UserPreference
) : PagingSource<Int, ListStoryItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val responseData = apiService.getStoriesPaging(position, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}