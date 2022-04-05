package ru.nifontbus.lc5_redditpaging.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.nifontbus.lc5_redditpaging.data.remote.RedditApi
import ru.nifontbus.lc5_redditpaging.model.RedditPost
import java.io.IOException

class RedditPagingSource(private val redditApi: RedditApi) :
    PagingSource<String, RedditPost>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditPost> {
        return try {
            val response = redditApi.fetchPosts(loadSize = params.loadSize)
            val listing = response.body()?.data
            val redditPosts = listing?.children?.map { it.data }
            LoadResult.Page(
                data = redditPosts ?: listOf(),
                prevKey = listing?.before,
                nextKey = listing?.after
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<String, RedditPost>): String {
        return state.anchorPosition.toString()
    }

    override val keyReuseSupported: Boolean = true
}