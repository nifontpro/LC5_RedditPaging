package ru.nifontbus.lc5_redditpaging.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.nifontbus.lc5_redditpaging.model.RedditPost

@Dao
interface RedditPostsDao {

    @Query("SELECT * FROM RedditPost")
    fun getAllPosts(): PagingSource<Int, RedditPost>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPosts(posts: List<RedditPost>)

    @Query("DELETE FROM RedditPost")
    suspend fun deleteAllPosts()

}