package projects.ferrari.rene.amalgamate.posts.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Children(
    val data: DataX,
    val kind: String
)
