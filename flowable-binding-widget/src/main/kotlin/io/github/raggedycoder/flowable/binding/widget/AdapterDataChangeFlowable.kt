package io.github.raggedycoder.flowable.binding.widget

import android.database.DataSetObserver
import android.widget.Adapter
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun <T : Adapter> T.dataChanges(skipInitial: Boolean = false) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial) offer(this@dataChanges)
    val dataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            if (isActive) offer(this@dataChanges)
        }
    }
    registerDataSetObserver(dataSetObserver)
    awaitClose { unregisterDataSetObserver(dataSetObserver) }
}

