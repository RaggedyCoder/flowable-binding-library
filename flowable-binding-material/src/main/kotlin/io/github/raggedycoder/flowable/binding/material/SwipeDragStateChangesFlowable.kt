package io.github.raggedycoder.flowable.binding.material

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.behavior.SwipeDismissBehavior.*
import io.github.raggedycoder.flowable.binding.isInMainThread
import io.github.raggedycoder.flowable.binding.material.SwipeDragState.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

enum class SwipeDragState {
    IDLE, DRAGGING, SETTLING, UNKNOWN
}

@ExperimentalCoroutinesApi
fun View.dragStateChanges() = callbackFlow<SwipeDragState> {
    if (!isInMainThread) return@callbackFlow
    val params = layoutParams as? CoordinatorLayout.LayoutParams
        ?: throw IllegalArgumentException("The view is not in a Coordinator Layout.")
    val behavior = params.behavior as SwipeDismissBehavior<*>?
        ?: throw IllegalStateException("There's no behavior set on this view.")
    behavior.listener = object : OnDismissListener {
        override fun onDismiss(view: View?) {

        }

        override fun onDragStateChanged(state: Int) {
            if (isActive) {
                when (state) {
                    STATE_IDLE -> offer(IDLE)
                    STATE_DRAGGING -> offer(DRAGGING)
                    STATE_SETTLING -> offer(SETTLING)
                    else -> offer(UNKNOWN)
                }
            }
        }
    }
    awaitClose { behavior.listener = null }
}
