package projects.ferrari.rene.amalgamate.posts.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import projects.ferrari.rene.amalgamate.posts.ui.filtergroup.GroupType
import projects.ferrari.rene.amalgamate.posts.ui.filtergroup.TimeFrame
import projects.ferrari.rene.amalgamate.posts.api.FetchedItem
import projects.ferrari.rene.amalgamate.posts.api.NoConnectionException

class PostsViewModel(private val groupType: GroupType) : ViewModel() {
    private val disposable = CompositeDisposable()

    private val mutableItems = MutableLiveData<List<FetchedItem>>()
    val items: LiveData<List<FetchedItem>>
        get() = mutableItems

    private val mutableState = MutableLiveData<State>()
    val state: LiveData<State>
        get() = mutableState

    private var timeFrameForFetchedData: TimeFrame? = null

    private fun fetchItems(timeFrame: TimeFrame? = null) {
        setUpFetchItems(timeFrame)

        disposable += Single
            .zip(
                groupType.fetchables.map { fetchable ->
                    fetchable.timeFrame = timeFrameForFetchedData ?: TimeFrame.DAY
                    fetchable.fetchItems()
                }
            ) { lists -> reduceAndSort(lists) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { items ->
                    mutableItems.value = items
                    mutableState.value =
                        State.LOADED
                },
                onError = { error ->
                    mutableState.value =
                        State.LOADED

                    mutableState.value = if (error is NoConnectionException) {
                        State.NO_CONNECTION
                    } else {
                        State.GENERIC_ERROR
                    }
                }
            )
    }

    //reducing array of lists to a single list and sorting it by votes (desc)
    private fun reduceAndSort(lists: Array<out Any>) = lists.mapNotNull { list ->
        (list as? List<*>)?.filterIsInstance<FetchedItem>()
    }.reduce { acc: List<FetchedItem>, list ->
        acc.toMutableList().apply {
            addAll(list)
        }
    }.sortedByDescending { item ->
        item.votes
    }

    private fun setUpFetchItems(timeFrame: TimeFrame?) {
        mutableState.value =
            State.INITIAL
        mutableItems.value = emptyList()
        timeFrameForFetchedData = timeFrame ?: timeFrameForFetchedData
        mutableState.value =
            State.LOADING
    }

    fun refetchDataIfNecessary(timeFrame: TimeFrame? = null) {
        timeFrame?.let { timeFrame ->
            if (timeFrame != timeFrameForFetchedData || items.value.isNullOrEmpty()) {
                fetchItems(timeFrame)
            }
        } ?: fetchItems() //if timeFrame is null -> initial fetch
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    enum class State {
        INITIAL,
        LOADING,
        LOADED,
        NO_CONNECTION,
        GENERIC_ERROR
    }
}
