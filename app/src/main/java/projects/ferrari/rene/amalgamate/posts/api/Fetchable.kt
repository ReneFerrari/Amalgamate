package projects.ferrari.rene.amalgamate.posts.api

import io.reactivex.rxjava3.core.Single
import projects.ferrari.rene.amalgamate.posts.ui.filtergroup.TimeFrame
import projects.ferrari.rene.amalgamate.core.isConnected

abstract class Fetchable(open var timeFrame: TimeFrame) {

    fun fetchItems(): Single<List<FetchedItem>> {
        if (!isConnected) {
            return Single.error(NoConnectionException())
        }

        return provideFetchRequest()
    }

    protected abstract fun provideFetchRequest(): Single<List<FetchedItem>>
}

class NoConnectionException : Exception(
    "An active internet connection is required for executing network requests."
)
