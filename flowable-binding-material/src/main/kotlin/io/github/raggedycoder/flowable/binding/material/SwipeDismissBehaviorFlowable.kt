package io.github.raggedycoder.flowable.binding.material

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.SwipeDismissBehavior
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun View.dismisses() = callbackFlow<Unit> {
    if (!isInMainThread) return@callbackFlow
    val params = layoutParams as? CoordinatorLayout.LayoutParams
        ?: throw IllegalArgumentException("The view is not in a Coordinator Layout.")
    val behavior = params.behavior as SwipeDismissBehavior<*>?
        ?: throw IllegalStateException("There's no behavior set on this view.")
    behavior.listener = object : SwipeDismissBehavior.OnDismissListener {
        override fun onDismiss(view: View?) {
            if (isActive) offer(Unit)
        }

        override fun onDragStateChanged(state: Int) {
        }
    }
    awaitClose { behavior.listener = null }
}
