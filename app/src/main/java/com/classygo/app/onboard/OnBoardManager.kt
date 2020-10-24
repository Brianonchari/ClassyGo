package com.classygo.app.onboard

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * @author by Lawrence on 10/24/20.
 * for ClassyGo
 */
class OnBoardingManager(private val sharedPref: SharedPreferences) {

    fun setAppsFirstLaunch(isFirstLaunch: Boolean) {
        sharedPref.edit(commit = true) { putBoolean(APP_FIRST_LAUNCH, isFirstLaunch) }
    }

    fun isFirstLaunch(): Boolean {
        return sharedPref.getBoolean(APP_FIRST_LAUNCH, true)
    }
}

private const val APP_FIRST_LAUNCH : String = "AppsFirstRun"