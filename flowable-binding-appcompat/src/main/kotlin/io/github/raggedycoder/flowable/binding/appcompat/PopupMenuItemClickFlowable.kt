package io.github.raggedycoder.flowable.binding.appcompat

import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun PopupMenu.itemClicks(handled: (MenuItem) -> Boolean) = callbackFlow<MenuItem> {
    if (!isInMainThread) return@callbackFlow
    setOnMenuItemClickListener {
        if (isActive) {
            try {
                if (handled(it)) {
                    offer(it)
                    return@setOnMenuItemClickListener true
                }
            } catch (ex: Exception) {

            }
        }
        return@setOnMenuItemClickListener false
    }

    awaitClose { setOnMenuItemClickListener(null) }
}
