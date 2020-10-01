package io.github.raggedycoder.flowable.binding.view

import android.view.View
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun View.layoutChanges() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    val listener = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        if (isActive) offer(Unit)
    }
    addOnLayoutChangeListener(listener)
    awaitClose { removeOnLayoutChangeListener(listener) }
}
