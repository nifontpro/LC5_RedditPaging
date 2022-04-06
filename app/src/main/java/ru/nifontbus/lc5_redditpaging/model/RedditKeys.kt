package ru.nifontbus.lc5_redditpaging.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RedditKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?,
    val after: String?
)
