package io.github.raggedycoder.flowable.binding.view

import android.view.View
import android.view.View.OnFocusChangeListener
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun View.focusChanges() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
        if (isActive) offer(hasFocus)
    }
    awaitClose { onFocusChangeListener = null }
}
