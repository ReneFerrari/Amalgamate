package projects.ferrari.rene.amalgamate.posts.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Topic(
    @Json(name = "posts_count") val postsCount: Int,
    @Json(name = "reply_count") val replyCount: Int,
    val title: String,
    val slug: String,
    val id: Int,
    val views: Int,
    val visible: Boolean
)
