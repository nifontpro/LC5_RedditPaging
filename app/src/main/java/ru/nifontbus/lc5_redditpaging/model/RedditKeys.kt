package ru.nifontbus.lc5_redditpaging.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "redditKeys")
data class RedditKeys(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val after: String?,
    val before: String?
)
