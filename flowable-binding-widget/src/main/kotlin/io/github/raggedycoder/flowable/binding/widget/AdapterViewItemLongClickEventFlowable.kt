package io.github.raggedycoder.flowable.binding.widget

import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

data class AdapterViewItemLongClickEvent(val clickedView: View, val position: Int, val id: Long)

@ExperimentalCoroutinesApi
fun <T : Adapter> AdapterView<T>.itemLongClickEvents(
    handled: (AdapterViewItemLongClickEvent) -> Boolean
) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    onItemLongClickListener = AdapterView.OnItemLongClickListener { _, view, position, id ->
        if (isActive) {
            val data = AdapterViewItemLongClickEvent(view, position, id)
            try {
                if (handled(data)) {
                    offer(data)
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
