package projects.ferrari.rene.amalgamate.core

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import kotlinx.android.synthetic.main.activity_main.*
import projects.ferrari.rene.amalgamate.R
import projects.ferrari.rene.amalgamate.core.extensions.colorStateList
import projects.ferrari.rene.amalgamate.core.extensions.preferences
import projects.ferrari.rene.amalgamate.posts.ui.AndroidPostsFragment
import projects.ferrari.rene.amalgamate.posts.ui.BaseFragment
import projects.ferrari.rene.amalgamate.posts.ui.IOSPostsFragment
import projects.ferrari.rene.amalgamate.posts.ui.filtergroup.GroupType
import projects.ferrari.rene.amalgamate.posts.ui.filtergroup.OnFilterChangedListener
import projects.ferrari.rene.amalgamate.splash.SplashFragment

class MainActivity : AppCompatActivity(R.layout.activity_main), SplashFragment.OnRemovedListener {

    private val fragmentsMap = mapOf(
        R.id.action_android to AndroidPostsFragment(),
        R.id.action_ios to IOSPostsFragment()
    )
    private val defaultGroupType = GroupType.ANDROID
    private var selectedId = R.id.action_android

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val hasSavedState = savedInstanceState != null

        blackenStatusBarPreMarshmallow()
        setBottomViewItemSelectedListener()
        setFilterGroupCheckedChangeListener()
        showSplashScreenIfRequired(hasSavedState)

        if (hasSavedState) {
            findFragment(bottomNavigation.selectedItemId)?.let { fragment ->
                filterGroup.showGroup(fragment.groupType)
            }
            return
        }
        setInitialState()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val groupName = savedInstanceState.getString(Constants.KEY_GROUP, defaultGroupType.name)
        selectedId = savedInstanceState.getInt(Constants.KEY_SELECTED_ID, fragmentsMap.keys.first())
        val groupType = GroupType.valueOf(groupName)
        filterGroup.showGroup(groupType)
        bottomNavigation.itemIconTintList = colorStateList(groupType.selector)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val groupName = findFragment(selectedId)?.groupType?.name ?: defaultGroupType.name
        outState.putString(Constants.KEY_GROUP, groupName)
        outState.putInt(Constants.KEY_SELECTED_ID, selectedId)
    }

    override fun onSplashFragmentRemoved() {
        bottomNavigation.visibility = View.VISIBLE
        preferences.edit {
            putBoolean(Constants.KEY_IS_SPLASH_DISPLAYED, false)
            putBoolean(Constants.KEY_SHOULD_SHOW_SPLASH, false)
        }
    }

    //lightStatusBar doesn't exist pre lollipop
    private fun blackenStatusBarPreMarshmallow() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            window.statusBarColor = Color.BLACK
        }
    }

    private fun showSplashScreenIfRequired(hasSavedInstance: Boolean) {
        val shouldShowSplash = preferences.getBoolean(Constants.KEY_SHOULD_SHOW_SPLASH, true)
        val isSplashDisplayed = preferences.getBoolean(Constants.KEY_IS_SPLASH_DISPLAYED, false)

        if (isSplashDisplayed || shouldShowSplash) {
            bottomNavigation.visibility = View.GONE
        }

        if (!hasSavedInstance && shouldShowSplash) {
            supportFragmentManager.commitNow {
                add(
                    R.id.clContainer,
                    SplashFragment()
                )
            }
            preferences.edit {
                putBoolean(Constants.KEY_IS_SPLASH_DISPLAYED, true)
            }
        }
    }

    private fun setBottomViewItemSelectedListener() {
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            if (bottomNavigation.selectedItemId == item.itemId) {
                return@setOnNavigationItemSelectedListener true
            }

            selectedId = item.itemId
            findFragment(selectedId)?.let { fragment ->
                replaceFragment(fragment, selectedId)
                filterGroup.showGroup(fragment.groupType)
                bottomNavigation.itemIconTintList = colorStateList(fragment.groupType.selector)
            }

            true
        }
    }

    private fun setFilterGroupCheckedChangeListener() {
        filterGroup.onCheckChanged { timeFrame ->
            val listener = findFragment(selectedId) as? OnFilterChangedListener
            listener?.onFilterChanged(timeFrame)
        }
    }

    private fun setInitialState() {
        val initialFragment = fragmentsMap.values.first()
        bottomNavigation.itemIconTintList = colorStateList(initialFragment.groupType.selector)
        replaceFragment(initialFragment, fragmentsMap.keys.first())
    }

    private fun replaceFragment(fragment: Fragment, id: Int) {
        supportFragmentManager.commitNow {
            replace(R.id.container, fragment, id.toString())
        }
    }

    private fun findFragment(id: Int): BaseFragment? {
        return supportFragmentManager
            .findFragmentByTag(id.toString()) as? BaseFragment
            ?: fragmentsMap[id]
    }
}
