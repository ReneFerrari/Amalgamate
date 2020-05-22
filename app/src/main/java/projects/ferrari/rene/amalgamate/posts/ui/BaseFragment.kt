package projects.ferrari.rene.amalgamate.posts.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_base.*
import projects.ferrari.rene.amalgamate.*
import projects.ferrari.rene.amalgamate.core.Constants
import projects.ferrari.rene.amalgamate.core.extensions.color
import projects.ferrari.rene.amalgamate.core.extensions.drawable
import projects.ferrari.rene.amalgamate.posts.vm.PostsViewModel.State
import projects.ferrari.rene.amalgamate.posts.ui.filtergroup.GroupType
import projects.ferrari.rene.amalgamate.posts.ui.filtergroup.OnFilterChangedListener
import projects.ferrari.rene.amalgamate.posts.ui.filtergroup.TimeFrame
import projects.ferrari.rene.amalgamate.posts.vm.PostsViewModel
import projects.ferrari.rene.amalgamate.posts.vm.PostsViewModelFactory

open class BaseFragment(val groupType: GroupType) :
    Fragment(R.layout.fragment_base),
    OnFilterChangedListener {

    private val defaultTimeFrameName = TimeFrame.DAY.name
    private val viewModel: PostsViewModel by viewModels {
        PostsViewModelFactory(groupType)
    }
    private val chromeCustomTabsIntent = CustomTabsIntent.Builder().build()

    private var currentTimeFrame: TimeFrame? = null
    private var shouldFetchWhenAttached = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeState()
        observeItems()

        btnRetry.setOnClickListener {
            viewModel.refetchDataIfNecessary()
        }
    }

    private fun observeItems() {
        viewModel.items.observe(viewLifecycleOwner) { items ->
            (rvItems.adapter as ItemAdapter).updateItems(items)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (shouldFetchWhenAttached || viewModel.items.value.isNullOrEmpty()) {
            viewModel.refetchDataIfNecessary(currentTimeFrame)
            shouldFetchWhenAttached = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        rvItems.layoutManager?.onSaveInstanceState()
        outState.putString(Constants.KEY_TIME_FRAME, currentTimeFrame?.name ?: defaultTimeFrameName)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        currentTimeFrame = TimeFrame.valueOf(
            savedInstanceState?.getString(Constants.KEY_TIME_FRAME) ?: defaultTimeFrameName
        )
        rvItems.layoutManager?.onRestoreInstanceState(savedInstanceState)
    }

    /*
    It is possible that onFilterChanged is invoked before the Fragment is attached. If this is
    the case, (possible) refetching will happen in the onAttach function.
     */
    override fun onFilterChanged(timeFrame: TimeFrame) {
        currentTimeFrame = timeFrame

        if (isAdded) {
            viewModel.refetchDataIfNecessary(currentTimeFrame)
        } else {
            shouldFetchWhenAttached = true
        }
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                State.INITIAL -> {
                    hideLoading()
                    hideError()
                }
                State.LOADING -> showLoading()
                State.LOADED -> hideLoading()
                State.NO_CONNECTION -> showNoConnection()
                State.GENERIC_ERROR -> showGenericError()
            }
        }
    }

    private fun setUpRecyclerView() = with(rvItems) {
        layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        adapter = ItemAdapter(
            requireContext(),
            groupType
        ) { urlStr ->
            chromeCustomTabsIntent.launchUrl(requireContext(), Uri.parse(urlStr))
        }
        addItemDecoration(
            DividerItemDecoration(
                context, (layoutManager as LinearLayoutManager).orientation
            )
        )
    }

    private fun showLoading() {
        color(groupType.accentColor)?.let { color ->
            pbLoadingItems.indeterminateDrawable.setTint(color)
        }
        pbLoadingItems.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        pbLoadingItems.visibility = View.GONE
    }

    private fun showNoConnection() {
        showError(
            R.drawable.ic_no_connection,
            R.string.error_no_connection
        )
    }

    private fun showGenericError() {
        showError(
            R.drawable.ic_error,
            R.string.error_generic
        )
    }

    private fun showError(@DrawableRes drawableId: Int, @StringRes message: Int) {
        color(groupType.accentColor)?.let { color ->
            btnRetry.setBackgroundColor(color)
        }
        drawable(drawableId)?.let { drawable ->
            ivError.background = drawable
        }
        tvError.text = getString(message)
        llError.visibility = View.VISIBLE
    }

    private fun hideError() {
        llError.visibility = View.GONE
    }
}
