package ru.nifontbus.lc5_redditpaging.model

class RedditListing(
    val children: List<PostContainer>,
    val after: String?,
    val before: String?
)