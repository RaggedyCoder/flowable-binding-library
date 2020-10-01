package io.github.raggedycoder.flowable.binding.view

import android.view.View
import android.view.ViewTreeObserver.OnDrawListener
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun View.draws() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    val listener = OnDrawListener { if (isActive) offer(Unit) }
    viewTreeObserver.addOnDrawListener(listener)
    awaitClose { viewTreeObserver.removeOnDrawListener(listener) }
}
