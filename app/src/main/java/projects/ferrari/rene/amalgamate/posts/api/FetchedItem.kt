package projects.ferrari.rene.amalgamate.posts.api

import androidx.annotation.ColorRes
import projects.ferrari.rene.amalgamate.R

data class FetchedItem(
    val origin: Origin,
    val title: String,
    val urlStr: String,
    val votes: Int
)

//Hardcoded titles because they are language independent
enum class Origin(@ColorRes val color: Int, val title: String) {
    ANDROID_DEV(R.color.colorAndroid, "androidDev"),
    KOTLIN_LANG(android.R.color.holo_orange_dark, "kotlinlang"),
    KOTLIN(android.R.color.holo_orange_light, "kotlin"),
    IOS_PROGRAMMING(R.color.colorIOS, "iOSProgramming"),
    SWIFT(R.color.colorSwift, "swift")
}
