package projects.ferrari.rene.amalgamate.posts.api

import com.squareup.moshi.Moshi
import io.reactivex.rxjava3.core.Single
import java.lang.NullPointerException
import okhttp3.Request
import projects.ferrari.rene.amalgamate.posts.ui.filtergroup.TimeFrame
import projects.ferrari.rene.amalgamate.core.client
import projects.ferrari.rene.amalgamate.posts.api.model.TopicList

class KotlinLangFetcher(override var timeFrame: TimeFrame = TimeFrame.DAY) : Fetchable(timeFrame) {

    private val url = "https://discuss.kotlinlang.org/top/%s.json?order=default"

    override fun provideFetchRequest(): Single<List<FetchedItem>> = Single.create { single ->
        val response = client.newCall(
            Request.Builder()
                .url(url.format(timeFrameAsAdverb()))
                .build()
        ).execute()

        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(TopicList::class.java)

        adapter.fromJson(response.body?.string() ?: "")?.let { topicList ->
            single.onSuccess(
                topicList.topicList.topics.map { topic ->
                    FetchedItem(
                        Origin.KOTLIN_LANG,
                        topic.title,
                        "https://discuss.kotlinlang.org/t/${topic.slug}/${topic.id}",
                        topic.views
                    )
                }
            )
        } ?: single.onError(NullPointerException())
    }

    private fun timeFrameAsAdverb() = when (timeFrame) {
        TimeFrame.DAY -> "daily"
        TimeFrame.WEEK -> "weekly"
        TimeFrame.MONTH -> "monthly"
        TimeFrame.YEAR -> "yearly"
        TimeFrame.ALL_TIME -> "all"
    }
}
