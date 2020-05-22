package projects.ferrari.rene.amalgamate.posts.ui.filtergroup

import androidx.annotation.StringRes
import projects.ferrari.rene.amalgamate.R

enum class TimeFrame(@StringRes val nameId: Int) {
    DAY(R.string.day),
    WEEK(R.string.week),
    MONTH(R.string.month),
    YEAR(R.string.year),
    ALL_TIME(R.string.all_time)
}
