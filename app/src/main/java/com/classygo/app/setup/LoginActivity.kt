package com.classygo.app.setup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.classygo.app.R
import com.classygo.app.trip.AllTripsActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()

        btnSignIn.setOnClickListener {
            loginUser()
        }

        goto_signup_tv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    private fun loginUser(){
        val emailAddress = login_email_address.text.toString()
        val password = login_password_et.text.toString()

        if( emailAddress.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    mAuth.signInWithEmailAndPassword(emailAddress,password).await()
                    withContext(Dispatchers.Main){
                        checkLoggedInState()
                    }
                }catch (ex: Exception){
                    withContext(Dispatchers.Main){
                         Toast.makeText(this@LoginActivity, ex.message  , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkLoggedInState() {
        if(mAuth.currentUser==null){
            Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(Intent(this, AllTripsActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }
}