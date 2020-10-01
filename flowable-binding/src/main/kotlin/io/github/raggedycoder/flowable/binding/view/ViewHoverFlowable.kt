package io.github.raggedycoder.flowable.binding.view

import android.view.MotionEvent
import android.view.View
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun View.hovers(handled: (MotionEvent) -> Boolean) = callbackFlow<MotionEvent> {
    if (!isInMainThread) return@callbackFlow
    setOnHoverListener { _, motionEvent ->
        if (isActive) {
            try {
                if (handled(motionEvent)) {
                    offer(motionEvent)
                    return@setOnHoverListener true
                }
            } catch (e: Exception) {
                cancel(e.message ?: "", e)
            }
        }
        return@setOnHoverListener false
    }

    awaitClose { setOnHoverListener(null) }
}
