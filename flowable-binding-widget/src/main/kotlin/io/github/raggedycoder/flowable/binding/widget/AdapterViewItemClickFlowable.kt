package io.github.raggedycoder.flowable.binding.widget

import android.widget.Adapter
import android.widget.AdapterView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun <T : Adapter> AdapterView<T>.itemClicks() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
        if (isActive) offer(position)
    }

    awaitClose { onItemClickListener = null }
}
