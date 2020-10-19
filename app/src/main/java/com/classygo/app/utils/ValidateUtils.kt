package com.classygo.app.utils

import androidx.core.widget.doOnTextChanged
import com.classygo.app.R
import com.google.android.material.textfield.TextInputLayout

/**
 * @author by Lawrence on 10/17/20.
 * for ClassyGo
 */

fun TextInputLayout.validateEmail(): String?{
    val value = this.editText?.text.toString()

    this.editText?.doOnTextChanged { _, _, _, _ ->
        if(this.isErrorEnabled){
            this.error = ""
            this.isErrorEnabled = false
        }
    }

    if (value.trim().isBlank()) {
        this.error = context.getString(R.string.email_required)
        this.isErrorEnabled = true
        return null
    }

    if (!isValidEmail(value)) {
        this.error = context.getString(R.string.invalid_email)
        this.isErrorEnabled = true
        return null
    }
    return value.trim()
}

private fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty()
            && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}