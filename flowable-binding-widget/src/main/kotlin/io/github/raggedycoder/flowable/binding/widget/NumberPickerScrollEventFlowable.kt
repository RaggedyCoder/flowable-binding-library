package io.github.raggedycoder.flowable.binding.widget

import android.widget.NumberPicker
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun NumberPicker.scrollEvents() = callbackFlow<Int> {
    if (!isInMainThread) return@callbackFlow
    setOnScrollListener { _, scrollState ->
        if (isActive) offer(scrollState)
    }

    awaitClose { setOnScrollListener(null) }
}
