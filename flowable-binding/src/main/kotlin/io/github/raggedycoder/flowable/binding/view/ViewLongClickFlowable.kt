package io.github.raggedycoder.flowable.binding.view

import android.view.View
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun View.longClicks(handled: () -> Boolean) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    setOnLongClickListener {
        if (handled()) {
            if (isActive) offer(Unit)
            true
        } else false
    }
    awaitClose { setOnLongClickListener(null) }
}
