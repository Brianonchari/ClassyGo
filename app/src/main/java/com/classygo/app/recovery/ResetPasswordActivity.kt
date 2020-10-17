package com.classygo.app.recovery

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.classygo.app.R
import com.classygo.app.startup.SplashActivity
import com.classygo.app.utils.validateEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        mbRequestReset.setOnClickListener { validateEmail() }
    }

    private fun validateEmail() {
        val mailAddress = tilEmailAddress.validateEmail()
        mailAddress?.let {
            requestPassWordReset(email = it)
        }
    }

    private fun requestPassWordReset(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    showSuccessDialog()
                }
            }.addOnFailureListener {
                if (it is FirebaseAuthInvalidUserException) {
                    showAlertDialog(getString(R.string.no_such_account))
                } else {
                    it.localizedMessage?.let { error -> showAlertDialog(error) }
                }
            }
    }

    private fun showSuccessDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.reset_mail_sent) )
        builder.setMessage(getString(R.string.reset_sent))
        builder.setPositiveButton(getString(R.string.back_to_login)) { dialogInterface, _ ->
            dialogInterface.dismiss()
            /**
             * Uncomment if LoginActivity Exists
             * **/
//            Intent(this, LoginActivity::class.java).apply {
//                this.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
//                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
//                        Intent.FLAG_ACTIVITY_NEW_TASK
//                startActivity(this)
//            }
        }
        builder.create().show()
    }

    private fun showAlertDialog(message: String, title: String = getString(R.string.error_title)) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)
        builder.create().show()
    }
}