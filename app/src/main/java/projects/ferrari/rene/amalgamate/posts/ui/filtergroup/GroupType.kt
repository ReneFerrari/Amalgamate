package projects.ferrari.rene.amalgamate.posts.ui.filtergroup

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import projects.ferrari.rene.amalgamate.R
import projects.ferrari.rene.amalgamate.posts.api.Fetchable
import projects.ferrari.rene.amalgamate.posts.api.KotlinLangFetcher
import projects.ferrari.rene.amalgamate.posts.api.Origin
import projects.ferrari.rene.amalgamate.posts.api.RedditFetcher

enum class GroupType(
    @ColorRes val selector: Int,
    @ColorRes val accentColor: Int,
    @DrawableRes val rippleDrawable: Int,
    val fetchables: List<Fetchable>
) {
    IOS(
        R.color.selector_bottom_nav_ios,
        R.color.colorIOS,
        R.drawable.ripple_ios,
        listOf(
            RedditFetcher(Origin.IOS_PROGRAMMING),
            RedditFetcher(Origin.SWIFT)
        )
    ),
    ANDROID(
        R.color.selector_bottom_nav_android,
        R.color.colorAndroid,
        R.drawable.ripple_android,
        listOf(
            KotlinLangFetcher(),
            RedditFetcher(Origin.ANDROID_DEV),
            RedditFetcher(Origin.KOTLIN)
        )
    )
}
