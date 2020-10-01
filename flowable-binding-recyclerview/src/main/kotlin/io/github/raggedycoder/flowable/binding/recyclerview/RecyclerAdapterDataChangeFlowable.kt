package io.github.raggedycoder.flowable.binding.recyclerview

import androidx.recyclerview.widget.RecyclerView.*
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun <T : Adapter<out ViewHolder>> T.dataChanges(skipInitial: Boolean = false) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial) offer(this@dataChanges)

    val listener = object : AdapterDataObserver() {
        override fun onChanged() {
            if (isActive) offer(this@dataChanges)
        }

    }
    registerAdapterDataObserver(listener)
    awaitClose { unregisterAdapterDataObserver(listener) }
}
