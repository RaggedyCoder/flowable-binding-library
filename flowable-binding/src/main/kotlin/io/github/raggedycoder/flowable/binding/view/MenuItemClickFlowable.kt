package io.github.raggedycoder.flowable.binding.view

import android.view.MenuItem
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun MenuItem.clicks(handled: (MenuItem) -> Boolean) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    setOnMenuItemClickListener {
        if (isActive) {
            try {
                if (handled(it)) {
                    offer(it)
                    return@setOnMenuItemClickListener true
                }
            } catch (ex: Exception) {
                cancel(ex.message ?: "", ex)
            }
        }
        return@setOnMenuItemClickListener false
    }
    awaitClose { setOnMenuItemClickListener(null) }
}
