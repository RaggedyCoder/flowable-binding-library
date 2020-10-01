package io.github.raggedycoder.flowable.binding.view

import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun View.globalLayouts() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    val listener = OnGlobalLayoutListener { if (isActive) offer(Unit) }
    viewTreeObserver.addOnGlobalLayoutListener(listener)
    awaitClose { viewTreeObserver.removeOnGlobalLayoutListener(listener) }
}
