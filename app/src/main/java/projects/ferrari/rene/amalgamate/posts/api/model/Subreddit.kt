package projects.ferrari.rene.amalgamate.posts.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Subreddit(
    val data: Data
)
