package io.github.raggedycoder.flowable.binding.recyclerview

import androidx.recyclerview.widget.RecyclerView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

data class RecyclerViewFlingEvent(val velocityX: Int, val velocityY: Int)

@ExperimentalCoroutinesApi
fun RecyclerView.flingEvents(
    handled: (RecyclerViewFlingEvent) -> Boolean
) = callbackFlow<RecyclerViewFlingEvent> {
    if (!isInMainThread) return@callbackFlow

    onFlingListener = object : RecyclerView.OnFlingListener() {
        override fun onFling(velocityX: Int, velocityY: Int): Boolean {
            if (isActive) {
                val data = RecyclerViewFlingEvent(velocityX, velocityY)
                try {
                    if (handled(data)) {
                        offer(data)
                        return true
                    }
                } catch (ex: Exception) {
                    cancel(ex.message ?: "", ex)
                }
            }
            return false
        }


    }
    awaitClose { onFlingListener = null }
}
