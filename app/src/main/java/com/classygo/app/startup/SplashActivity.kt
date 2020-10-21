package com.classygo.app.startup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper


import com.classygo.app.R
import com.google.firebase.auth.FirebaseAuth



class SplashActivity : AppCompatActivity() {
    private val splashTime = 3000L
    private lateinit var mUser: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            mUser = FirebaseAuth.getInstance()
            mUser.signOut()
            if (mUser.currentUser != null) {
                val tripsIntent = Intent(this@SplashActivity, Class.forName("TripsActivity"))
                startActivity(tripsIntent)
                finish()
            } else {
                val tutorialIntent = Intent(this@SplashActivity, Class.forName("TutorialActivity"))
                startActivity(tutorialIntent)
                finish()
            }


        }, splashTime)
    }
}