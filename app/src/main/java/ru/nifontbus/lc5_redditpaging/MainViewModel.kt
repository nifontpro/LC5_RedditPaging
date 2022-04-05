package ru.nifontbus.lc5_redditpaging

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.nifontbus.lc5_redditpaging.data.repository.Repository
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class MainViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {
    val posts = repository.fetchPosts()
}