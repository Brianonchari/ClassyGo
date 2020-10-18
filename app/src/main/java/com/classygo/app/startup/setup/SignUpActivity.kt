package com.classygo.app.startup.setup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.classygo.app.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mAuth = FirebaseAuth.getInstance()
        btnSignUp.setOnClickListener {
            registerUser()
        }

        goto_login_tv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }


    private fun registerUser() {
        val firstName = first_nameEt.text.toString()
        val lastName = last_nameEt.text.toString()
        val emailAddress = signup_email_address_et.text.toString()
        val password = signup_passwordEt.text.toString()

        if ( emailAddress.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    mAuth.createUserWithEmailAndPassword(emailAddress, password)
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SignUpActivity, ex.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkLoggedInState() {
        if (mAuth.currentUser == null) {
            Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "You are logged in", Toast.LENGTH_SHORT).show()
        }
    }
}