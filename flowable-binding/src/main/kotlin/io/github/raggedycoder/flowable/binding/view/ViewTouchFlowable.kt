package io.github.raggedycoder.flowable.binding.view

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
@SuppressLint("ClickableViewAccessibility")
fun View.touches(handled: (MotionEvent) -> Boolean) = callbackFlow<MotionEvent> {
    if (!isInMainThread) return@callbackFlow
    setOnTouchListener { _, motionEvent ->
        if (isActive) {
            try {
                if (handled(motionEvent)) {
                    offer(motionEvent)
                    return@setOnTouchListener true
                }
            } catch (ex: Exception) {
                cancel(ex.message ?: "", ex)
            }
        }
        false
    }
    awaitClose { setOnTouchListener(null) }
}
