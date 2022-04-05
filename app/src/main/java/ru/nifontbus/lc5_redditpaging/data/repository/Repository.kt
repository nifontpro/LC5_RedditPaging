package ru.nifontbus.lc5_redditpaging.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.nifontbus.lc5_redditpaging.data.local.RedditDatabase
import ru.nifontbus.lc5_redditpaging.data.paging.RedditRemoteMediator
import ru.nifontbus.lc5_redditpaging.data.remote.RedditApi
import ru.nifontbus.lc5_redditpaging.model.RedditPost
import javax.inject.Inject

@ExperimentalPagingApi
class Repository @Inject constructor(
    private val redditApi: RedditApi,
    private val redditDatabase: RedditDatabase
) {
    fun fetchPosts(): Flow<PagingData<RedditPost>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = RedditRemoteMediator(redditApi, redditDatabase),
            pagingSourceFactory = { redditDatabase.redditPostsDao().getPosts() }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 40
    }
}