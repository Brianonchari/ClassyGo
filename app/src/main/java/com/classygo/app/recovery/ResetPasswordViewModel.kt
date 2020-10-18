package com.classygo.app.recovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.classygo.app.utils.EventUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException

/**
 * @author by Lawrence on 10/17/20.
 * for ClassyGo
 */
class ResetPasswordViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _requestReset = MutableLiveData<EventUtils<ResetPasswordState>>()
    val requestReset: LiveData<EventUtils<ResetPasswordState>> = _requestReset

    fun requestPassWordReset(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    _requestReset.value = EventUtils(ResetSuccess)
                }
            }.addOnFailureListener {
                if (it is FirebaseAuthInvalidUserException) {
                    _requestReset.value = EventUtils(InvalidUser)
                } else {
                    it.localizedMessage?.let {error ->
                        _requestReset.value = EventUtils(RequestError(error))
                    }
                }
            }
    }

}