package com.classygo.app.startup

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.classygo.app.R
import com.classygo.app.onboard.OnBoardActivity
import com.classygo.app.onboard.OnBoardingManager
import com.classygo.app.setup.LoginActivity
import com.classygo.app.trip.AllTripsActivity
import com.classygo.app.utils.InjectorUtils
import com.classygo.app.utils.launchActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private val splashTime = 3000L
    private lateinit var mUser: FirebaseAuth
    private val sharedPreferences by lazy { InjectorUtils.provideSharedPreference(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val boardingManager = OnBoardingManager(sharedPreferences)

        Handler(Looper.getMainLooper()).postDelayed({
            if (boardingManager.isFirstLaunch()) {
                boardingManager.setAppsFirstLaunch(false)
                launchActivity<OnBoardActivity>()
                finish()
            } else {
                mUser = FirebaseAuth.getInstance()
                mUser.signOut()
                if (mUser.currentUser != null) {
                    launchActivity<AllTripsActivity>()
                    finish()
                } else {
                    launchActivity<LoginActivity>()
                    finish()
                }
            }
        }, splashTime)
    }
}