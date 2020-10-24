package com.classygo.app.utils

import android.content.Context
import android.content.SharedPreferences
import com.classygo.app.R

/**
 * @author by Lawrence on 10/24/20.
 * for ClassyGo
 */
object InjectorUtils {
    fun provideSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.newly_install), Context.MODE_PRIVATE)
    }
}