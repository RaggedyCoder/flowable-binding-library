package io.github.raggedycoder.flowable.binding.recyclerview

import androidx.customview.widget.ViewDragHelper.*
import androidx.recyclerview.widget.RecyclerView
import io.github.raggedycoder.flowable.binding.isInMainThread
import io.github.raggedycoder.flowable.binding.recyclerview.RecyclerViewScrollState.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

enum class RecyclerViewScrollState {
    IDLE, DRAGGING, SETTLING, UNKNOWN
}

@ExperimentalCoroutinesApi
fun RecyclerView.scrollStateChanges() = callbackFlow<RecyclerViewScrollState> {
    if (!isInMainThread) return@callbackFlow

    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (isActive) {
                when (newState) {
                    STATE_IDLE -> offer(IDLE)
                    STATE_DRAGGING -> offer(DRAGGING)
                    STATE_SETTLING -> offer(SETTLING)
                    else -> offer(UNKNOWN)
                }
            }
        }
    }
    addOnScrollListener(listener)
    awaitClose { removeOnScrollListener(listener) }
}
