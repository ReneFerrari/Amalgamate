package projects.ferrari.rene.amalgamate.posts.api

import com.squareup.moshi.Moshi
import io.reactivex.rxjava3.core.Single
import java.util.Locale
import okhttp3.Request
import projects.ferrari.rene.amalgamate.posts.ui.filtergroup.TimeFrame
import projects.ferrari.rene.amalgamate.core.client
import projects.ferrari.rene.amalgamate.posts.api.model.Subreddit

open class RedditFetcher(
    private val origin: Origin,
    override var timeFrame: TimeFrame = TimeFrame.DAY
) : Fetchable(timeFrame) {
    private val moshi = Moshi.Builder().build()

    private var urlStrWithPlaceholder = "https://www.reddit.com/r/${origin.title}/top/.json?t=%s"

    override fun provideFetchRequest(): Single<List<FetchedItem>> = Single.create { single ->
        val response = client.newCall(
            Request.Builder()
                .url(urlStrWithPlaceholder.format(timeFrame.name.toLowerCase(Locale.getDefault())))
                .build()
        ).execute()

        val adapter = moshi.adapter(Subreddit::class.java)

        adapter.fromJson(response.body?.string() ?: "")?.let { subreddit ->
            single.onSuccess(
                subreddit.data.children.map { child ->
                    FetchedItem(
                        origin,
                        child.data.title,
                        "https://reddit.com${child.data.permalink}",
                        child.data.ups - child.data.downs
                    )
                }
            )
        }
    }
}
