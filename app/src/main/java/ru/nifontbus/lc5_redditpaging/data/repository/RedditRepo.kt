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
class RedditRepo @Inject constructor(
    private val redditService: RedditApi,
    private val redditDatabase: RedditDatabase
) {
    fun fetchPosts(): Flow<PagingData<RedditPost>> {
        return Pager(
            PagingConfig(pageSize = 40, enablePlaceholders = false, prefetchDistance = 3),
            remoteMediator = RedditRemoteMediator(redditService, redditDatabase),
            pagingSourceFactory = { redditDatabase.redditPostsDao().getPosts() }
        ).flow
    }
}