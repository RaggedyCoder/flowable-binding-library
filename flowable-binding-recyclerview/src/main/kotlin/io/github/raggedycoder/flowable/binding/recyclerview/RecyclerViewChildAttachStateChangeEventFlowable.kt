package io.github.raggedycoder.flowable.binding.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import io.github.raggedycoder.flowable.binding.isInMainThread
import io.github.raggedycoder.flowable.binding.recyclerview.RecyclerViewChildAttachStateChangeEvent
.RecyclerViewChildAttachEvent
import io.github.raggedycoder.flowable.binding.recyclerview.RecyclerViewChildAttachStateChangeEvent
.RecyclerViewChildDetachEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

sealed class RecyclerViewChildAttachStateChangeEvent {
    abstract val view: View

    data class RecyclerViewChildAttachEvent(override val view: View) :
        RecyclerViewChildAttachStateChangeEvent()

    data class RecyclerViewChildDetachEvent(override val view: View) :
        RecyclerViewChildAttachStateChangeEvent()
}

@ExperimentalCoroutinesApi
fun RecyclerView.childAttachStateChangeEvents(

) = callbackFlow<RecyclerViewChildAttachStateChangeEvent> {
    if (!isInMainThread) return@callbackFlow

    val listener = object : OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            sendEvent(RecyclerViewChildAttachEvent(view))
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            sendEvent(RecyclerViewChildDetachEvent(view))
        }

        private fun sendEvent(event: RecyclerViewChildAttachStateChangeEvent) {
            if (isActive) offer(event)
        }

    }
    addOnChildAttachStateChangeListener(listener)
    awaitClose { removeOnChildAttachStateChangeListener(listener) }
}
