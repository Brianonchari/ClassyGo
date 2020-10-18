package com.classygo.app.startup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler


import com.classygo.app.R
import com.classygo.app.startup.setup.LoginActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIMEOUT = 3000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val i = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(i)
            finish()

        }, SPLASH_TIMEOUT)
    }
}