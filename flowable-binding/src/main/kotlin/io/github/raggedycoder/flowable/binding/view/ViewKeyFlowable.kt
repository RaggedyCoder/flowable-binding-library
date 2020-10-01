package io.github.raggedycoder.flowable.binding.view

import android.view.KeyEvent
import android.view.View
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun View.keys(handled: (KeyEvent) -> Boolean) = callbackFlow<KeyEvent> {
    if (!isInMainThread) return@callbackFlow
    setOnKeyListener { _, _, event ->
        if (isActive) {
            try {
                if (handled(event)) {
                    offer(event)
                    return@setOnKeyListener true
                }
            } catch (e: Exception) {
                cancel(e.message ?: "", e)
            }
        }
        return@setOnKeyListener false
    }

    awaitClose { setOnKeyListener(null) }
}
