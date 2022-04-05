package ru.nifontbus.lc5_redditpaging.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.nifontbus.lc5_redditpaging.model.RedditKeys

@Dao
interface RedditKeysDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveRedditKeys(redditKey: RedditKeys)

    @Query("SELECT * FROM redditKeys ORDER BY id DESC")
    suspend fun getRedditKeys(): List<RedditKeys>

}