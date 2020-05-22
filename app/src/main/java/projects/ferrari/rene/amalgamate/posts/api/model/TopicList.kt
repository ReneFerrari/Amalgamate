package projects.ferrari.rene.amalgamate.posts.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TopicList(
    @Json(name = "topic_list") val topicList: KotlinLangResult
)
