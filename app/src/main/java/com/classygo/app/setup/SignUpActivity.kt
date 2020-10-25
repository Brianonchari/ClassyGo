package com.classygo.app.setup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.classygo.app.R
import com.classygo.app.model.Profile
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    private val profileCollectionRef = Firebase.firestore.collection("profile")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mAuth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            val firstName = first_nameEt.text.toString()
            val lastName = last_nameEt.text.toString()
            val emailAddress = signup_email_address_et.text.toString()
            val isDriver = true
            if(driver_checkbox.isChecked){
                val profile = Profile(firstName, lastName, emailAddress, isDriver)
                saveProfile(profile)
            }else{
                val profile = Profile(firstName, lastName, emailAddress, !isDriver)
                saveProfile(profile)
            }
            registerUser()
        }

        goto_login_tv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun saveProfile(profile: Profile) = CoroutineScope(Dispatchers.IO).launch {
        try {
            profileCollectionRef.add(profile).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Profile saved successfully",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } catch (ex: Exception) {
            withContext(Dispatchers.Main){
                Toast.makeText(this@SignUpActivity, "${ex.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser() {
        val emailAddress = signup_email_address_et.text.toString()
        val password = signup_passwordEt.text.toString()

        if (emailAddress.isNotEmpty() && password.isNotEmpty()) {
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