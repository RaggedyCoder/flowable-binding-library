package io.github.raggedycoder.flowable.binding.widget

import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

data class AdapterViewItemClickEvent(val clickedView: View, val position: Int, val id: Long)

@ExperimentalCoroutinesApi
fun <T : Adapter> AdapterView<T>.itemClickEvents() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    onItemClickListener = AdapterView.OnItemClickListener { _, view, position, id ->
        if (isActive) offer(AdapterViewItemClickEvent(view, position, id))
    }

    awaitClose { onItemClickListener = null }
}
