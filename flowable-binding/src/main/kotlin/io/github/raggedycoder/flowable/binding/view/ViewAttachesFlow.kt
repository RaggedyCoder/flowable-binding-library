package io.github.raggedycoder.flowable.binding.view

import android.view.View
import android.view.View.OnAttachStateChangeListener
import io.github.raggedycoder.flowable.binding.isInMainThread
import io.github.raggedycoder.flowable.binding.view.AttachState.ATTACHED
import io.github.raggedycoder.flowable.binding.view.AttachState.DETACHED
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

enum class AttachState {
    ATTACHED, DETACHED
}

@ExperimentalCoroutinesApi
private fun View.attachStates() = callbackFlow<AttachState> {
    if (!isInMainThread) return@callbackFlow

    val listener = object : OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View?) {
            if (isActive) offer(ATTACHED)
        }

        override fun onViewDetachedFromWindow(v: View?) {
            if (isActive) offer(DETACHED)
        }
    }
    addOnAttachStateChangeListener(listener)

    awaitClose { removeOnAttachStateChangeListener(listener) }
}
