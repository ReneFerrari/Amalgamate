package projects.ferrari.rene.amalgamate.core.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/*
Helpers to get resources in a more convenient way.
 */

fun Context.colorStateList(@ColorRes id: Int) = ContextCompat.getColorStateList(this, id)

fun Context.color(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Context.drawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

/*
For Fragment helpers nullable context is used, because it is not always guarenteed that it is
not null.
 */
fun Fragment.color(@ColorRes id: Int) = context?.color(id)

fun Fragment.drawable(@DrawableRes id: Int) = context?.drawable(id)
