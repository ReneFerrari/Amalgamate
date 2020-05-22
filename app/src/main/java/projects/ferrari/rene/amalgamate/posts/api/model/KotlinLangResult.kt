package projects.ferrari.rene.amalgamate.posts.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KotlinLangResult(
    val topics: List<Topic>
)
