package io.github.raggedycoder.flowable.binding.widget

import android.widget.Adapter
import android.widget.AdapterView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun <T : Adapter> AdapterView<T>.itemLongClicks(handled: (Int) -> Boolean) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, position, _ ->
        if (isActive) {
            try {
                if (handled(position)) {
                    offer(position)
                    return@OnItemLongClickListener true
                }
            } catch (ex: Exception) {
                cancel(ex.message ?: "", ex)
            }
        }
        return@OnItemLongClickListener false
    }

    awaitClose { onItemLongClickListener = null }
}
