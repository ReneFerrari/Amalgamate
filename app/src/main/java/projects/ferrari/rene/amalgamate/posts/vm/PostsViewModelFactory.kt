package projects.ferrari.rene.amalgamate.posts.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import projects.ferrari.rene.amalgamate.posts.ui.filtergroup.GroupType

class PostsViewModelFactory(private val groupType: GroupType) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostsViewModel(groupType) as T
    }
}
