package com.classygo.app.recovery

/**
 * @author by Lawrence on 10/17/20.
 * for ClassyGo
 */
sealed class ResetPasswordState()
object ResetSuccess : ResetPasswordState()
data class RequestError(val error: String) : ResetPasswordState()
object InvalidUser : ResetPasswordState()