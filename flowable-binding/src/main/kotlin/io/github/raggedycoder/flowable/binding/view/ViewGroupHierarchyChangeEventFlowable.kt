package io.github.raggedycoder.flowable.binding.view

import android.view.View
import android.view.ViewGroup
import io.github.raggedycoder.flowable.binding.isInMainThread
import io.github.raggedycoder.flowable.binding.view.ViewGroupHierarchyChangeEvent
.ViewGroupHierarchyChildViewAddEvent
import io.github.raggedycoder.flowable.binding.view.ViewGroupHierarchyChangeEvent
.ViewGroupHierarchyChildViewRemoveEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

sealed class ViewGroupHierarchyChangeEvent {
    abstract val child: View

    data class ViewGroupHierarchyChildViewAddEvent(override val child: View) :
        ViewGroupHierarchyChangeEvent()

    data class ViewGroupHierarchyChildViewRemoveEvent(override val child: View) :
        ViewGroupHierarchyChangeEvent()
}

@ExperimentalCoroutinesApi
fun ViewGroup.changeEvents() = callbackFlow<ViewGroupHierarchyChangeEvent> {
    if (!isInMainThread) return@callbackFlow
    setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
        override fun onChildViewAdded(parent: View, child: View) {
            if (isActive) offer(ViewGroupHierarchyChildViewAddEvent(child))
        }

        override fun onChildViewRemoved(parent: View, child: View) {
            if (isActive) offer(ViewGroupHierarchyChildViewRemoveEvent(child))
        }
    })
    awaitClose { setOnHierarchyChangeListener(null) }
}
