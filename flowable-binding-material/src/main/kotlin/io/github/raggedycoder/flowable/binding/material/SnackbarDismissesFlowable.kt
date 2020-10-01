package io.github.raggedycoder.flowable.binding.material

import com.google.android.material.snackbar.Snackbar
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun Snackbar.dismisses() = callbackFlow<Int> {
    if (!isInMainThread) return@callbackFlow
    val callback = object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar, event: Int) {
            if (isActive) offer(event)
        }
    }
    addCallback(callback)
    awaitClose { removeCallback(callback) }
}
