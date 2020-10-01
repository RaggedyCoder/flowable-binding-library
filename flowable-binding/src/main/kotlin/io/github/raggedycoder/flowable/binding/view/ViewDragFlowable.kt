package io.github.raggedycoder.flowable.binding.view

import android.view.DragEvent
import android.view.View
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun View.drags(handled: (DragEvent) -> Boolean) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    setOnDragListener { _, dragEvent ->
        if (isActive) {
            try {
                if (handled(dragEvent)) {
                    offer(dragEvent)
                    return@setOnDragListener true
                }
            } catch (ex: Exception) {
                cancel(ex.message ?: "", ex)
            }
        }
        return@setOnDragListener false
    }
    awaitClose { setOnClickListener(null) }
}
