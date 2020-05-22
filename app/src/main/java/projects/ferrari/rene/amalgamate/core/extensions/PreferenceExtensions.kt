package projects.ferrari.rene.amalgamate.core.extensions

import android.content.Context
import android.content.SharedPreferences
import projects.ferrari.rene.amalgamate.core.Constants

//Tiny helper to access SharedPreferences with ease
val Context.preferences: SharedPreferences
    get() = getSharedPreferences(Constants.FILE_NAME, Context.MODE_PRIVATE)
