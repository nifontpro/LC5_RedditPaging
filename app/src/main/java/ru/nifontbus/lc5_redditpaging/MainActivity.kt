package ru.nifontbus.lc5_redditpaging

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import dagger.hilt.android.AndroidEntryPoint
import ru.nifontbus.lc5_redditpaging.model.RedditPost
import ru.nifontbus.lc5_redditpaging.ui.theme.LC5_RedditPagingTheme
import ru.nifontbus.lc5_redditpaging.ui.theme.YellowStar

@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LC5_RedditPagingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Posts()
                }
            }
        }
    }

    @Composable
    fun Posts() {
        val viewModel: MainViewModel = hiltViewModel()
        val posts = viewModel.posts.collectAsLazyPagingItems()
        Scaffold {
            ListContent(posts = posts)
        }
    }

    @Composable
    private fun ListContent(posts: LazyPagingItems<RedditPost>) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.surface),
            contentPadding = PaddingValues(all = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = posts,
                key = { post ->
                    post.id
                }
            ) { post ->
                post?.let { PostItem(post) }
            }
        }
    }

    @Composable
    private fun PostItem(post: RedditPost) {
        Column(
            modifier = Modifier.fillMaxWidth()
        )
        {
            Text(
                text = post.author,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = post.title,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StarsCount(post.score)
                CommentCount(post.commentCount)
            }
            Divider()
        }
    }

    @Composable
    private fun StarsCount(score: Int) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_star),
                contentDescription = "Stars",
                tint = YellowStar,
                modifier = Modifier.padding(end = 12.dp)
            )
            Text(
                text = score.toString(),
                color = MaterialTheme.colors.onSurface
            )
        }
    }

    @Composable
    private fun CommentCount(commentCount: Int) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_comment),
                contentDescription = "Comments",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.padding(end = 12.dp)
            )
            Text(
                text = commentCount.toString(),
                modifier = Modifier.width(30.dp),
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}