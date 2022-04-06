package ru.nifontbus.lc5_redditpaging.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.nifontbus.lc5_redditpaging.model.RedditKeys

@Dao
interface RedditKeysDao {

    @Query("SELECT * FROM RedditKeys WHERE id =:id")
    suspend fun getRemoteKeys(id: String): RedditKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<RedditKeys>)

    @Query("DELETE FROM RedditKeys")
    suspend fun deleteAllRemoteKeys()

}