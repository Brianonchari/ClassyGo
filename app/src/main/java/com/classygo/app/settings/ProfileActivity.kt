package com.classygo.app.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.classygo.app.R


import com.classygo.app.onboard.OnBoardActivity
import com.classygo.app.utils.launchActivity
import com.google.firebase.auth.FirebaseAuth

import com.classygo.app.payment.PaymentMethods
import kotlinx.android.synthetic.main.activity_new_trip.toolbar
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar.*


class ProfileActivity : AppCompatActivity() {


    private lateinit var mCurrentUser: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        textViewTitle.text = getString(R.string.title_profile)


        cardViewNotification.setOnClickListener {
            launchActivity<NotificationActivity> { }
        }

        cardViewPaymentMethods.setOnClickListener {
            launchActivity<PaymentMethods> { }
        }

        cardViewSecurity.setOnClickListener {
            changePassword()
        }

        cardViewProfile.setOnClickListener {

        }

        cardViewLogOut.setOnClickListener {
            logOut()
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changePassword() {
        launchActivity<ChangePasswordActivity>()
    }

    private fun logOut() {
        mCurrentUser = FirebaseAuth.getInstance()
        mCurrentUser.signOut()
        launchActivity<OnBoardActivity>()
        finish()
    }
}