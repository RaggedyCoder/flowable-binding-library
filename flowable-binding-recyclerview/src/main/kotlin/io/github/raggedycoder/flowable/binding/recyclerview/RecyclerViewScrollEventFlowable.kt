package io.github.raggedycoder.flowable.binding.recyclerview

import androidx.recyclerview.widget.RecyclerView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

data class RecyclerViewScrollEvent(val velocityX: Int, val velocityY: Int)

@ExperimentalCoroutinesApi
fun RecyclerView.scrollEvents() = callbackFlow<RecyclerViewScrollEvent> {
    if (!isInMainThread) return@callbackFlow

    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (isActive) offer(RecyclerViewScrollEvent(dx, dy))
        }
    }
    addOnScrollListener(listener)
    awaitClose { removeOnScrollListener(listener) }
}
