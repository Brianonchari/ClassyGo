package com.classygo.app.utils

import androidx.lifecycle.Observer

/**
 * @author by Lawrence on 10/17/20.
 * for ClassyGo
 */
open class EventUtils<out T>(private val content : T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            content
        }
    }

    fun peekContent() : T = content
}

class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<EventUtils<T>> {
    override fun onChanged(event: EventUtils<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}