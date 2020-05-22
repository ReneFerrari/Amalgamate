package projects.ferrari.rene.amalgamate.core

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import projects.ferrari.rene.amalgamate.core.extensions.preferences

/*
Applies the correct Theme (dark or light). Default used is dark, however the user
can change the App Theme to light if he wants to.
 */
object AppThemer {
    private const val DEFAULT_MODE = AppCompatDelegate.MODE_NIGHT_YES

    fun theme(context: Context) {
        AppCompatDelegate.setDefaultNightMode(
            context.preferences.getInt(
                Constants.KEY_THEME_TYPE,
                DEFAULT_MODE
            )
        )
    }

    fun inverseTheme(context: Context) {
        val inversedMode =
            getInversedMode(
                context
            )
        context.preferences.edit {
            putInt(Constants.KEY_THEME_TYPE, inversedMode)
        }

        AppCompatDelegate.setDefaultNightMode(inversedMode)
    }

    private fun getInversedMode(context: Context): Int {
        return when (context.preferences.getInt(
            Constants.KEY_THEME_TYPE,
            DEFAULT_MODE
        )) {
            AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }
}
