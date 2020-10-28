package com.classygo.app.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.classygo.app.R
import com.classygo.app.databinding.ActivityProfileBinding
import com.classygo.app.onboard.OnBoardActivity
import com.classygo.app.utils.launchActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_new_trip.*
import kotlinx.android.synthetic.main.activity_new_trip.toolbar


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var mCurrentUser : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        textViewTitle.text = getString(R.string.title_profile)
        changePassword()
        logOut()



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changePassword() {
        binding.cardViewSecurity.setOnClickListener {
            launchActivity<ChangePasswordActivity>()
            finish()
        }

    }

    private fun logOut() {
        mCurrentUser = FirebaseAuth.getInstance()
        binding.cardViewLogOut.setOnClickListener {
            mCurrentUser.signOut()
            launchActivity<OnBoardActivity>()
            finish()
        }

    }




}