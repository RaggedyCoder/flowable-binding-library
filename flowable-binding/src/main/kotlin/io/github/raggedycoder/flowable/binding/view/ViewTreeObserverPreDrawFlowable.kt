package io.github.raggedycoder.flowable.binding.view

import android.view.View
import android.view.ViewTreeObserver.OnPreDrawListener
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun View.preDraws(proceedDrawingPass: () -> Boolean) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    val listener = OnPreDrawListener {
        if (isActive) {
            offer(Unit)
            try {
                return@OnPreDrawListener proceedDrawingPass()
            } catch (e: Exception) {
                cancel(e.message ?: "", e)
            }
        }
        true
    }
    viewTreeObserver.addOnPreDrawListener(listener)
    awaitClose { viewTreeObserver.removeOnPreDrawListener(listener) }
}
