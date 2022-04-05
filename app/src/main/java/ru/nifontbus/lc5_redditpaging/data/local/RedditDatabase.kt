package ru.nifontbus.lc5_redditpaging.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.nifontbus.lc5_redditpaging.data.local.dao.RedditKeysDao
import ru.nifontbus.lc5_redditpaging.data.local.dao.RedditPostsDao
import ru.nifontbus.lc5_redditpaging.model.RedditKeys
import ru.nifontbus.lc5_redditpaging.model.RedditPost

@Database(
    entities = [RedditPost::class, RedditKeys::class],
    version = 1,
    exportSchema = false
)
abstract class RedditDatabase : RoomDatabase() {
    abstract fun redditPostsDao(): RedditPostsDao
    abstract fun redditKeysDao(): RedditKeysDao
}