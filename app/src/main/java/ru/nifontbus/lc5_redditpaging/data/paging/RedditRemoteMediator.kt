package ru.nifontbus.lc5_redditpaging.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.nifontbus.lc5_redditpaging.data.local.RedditDatabase
import ru.nifontbus.lc5_redditpaging.data.remote.RedditApi
import ru.nifontbus.lc5_redditpaging.model.RedditKeys
import ru.nifontbus.lc5_redditpaging.model.RedditPost

@ExperimentalPagingApi
class RedditRemoteMediator(
    private val redditApi: RedditApi,
    private val redditDatabase: RedditDatabase
) : RemoteMediator<Int, RedditPost>() {

    private val postsDao = redditDatabase.redditPostsDao()
    private val keysDao = redditDatabase.redditKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RedditPost>
    ): MediatorResult {
        return try {
            var after = ""
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    after = remoteKeys?.after ?: ""
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    after = remoteKeys?.after ?: ""
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = redditApi.fetchPosts(
                loadSize = state.config.pageSize,
                after = after,
            )
            val listing = response.data
            val redditPosts = listing.children.map { it.data }

            val endOfPaginationReached = redditPosts.isNullOrEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            redditDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    postsDao.deleteAllPosts()
                    keysDao.deleteAllRemoteKeys()
                }
                val keys = redditPosts.map { post ->
                    RedditKeys(
                        id = post.id,
                        prevPage = prevPage,
                        nextPage = nextPage,
                        after = listing.after
                    )
                }
                keysDao.addAllRemoteKeys(remoteKeys = keys)
                postsDao.addPosts(posts = redditPosts)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, RedditPost>
    ): RedditKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                keysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, RedditPost>
    ): RedditKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { unsplashImage ->
                keysDao.getRemoteKeys(id = unsplashImage.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, RedditPost>
    ): RedditKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { unsplashImage ->
                keysDao.getRemoteKeys(id = unsplashImage.id)
            }
    }
}