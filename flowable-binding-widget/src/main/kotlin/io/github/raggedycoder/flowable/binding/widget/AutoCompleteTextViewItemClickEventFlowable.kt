package io.github.raggedycoder.flowable.binding.widget

import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun AutoCompleteTextView.itemClickEvents() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    onItemClickListener = AdapterView.OnItemClickListener { _, view, position, id ->
        if (isActive) offer(AdapterViewItemClickEvent(view, position, id))
    }

    awaitClose { onItemClickListener = null }
}
