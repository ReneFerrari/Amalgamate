package projects.ferrari.rene.amalgamate.posts.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataX(
    val downs: Int,
    val title: String,
    val ups: Int,
    val permalink: String
)
