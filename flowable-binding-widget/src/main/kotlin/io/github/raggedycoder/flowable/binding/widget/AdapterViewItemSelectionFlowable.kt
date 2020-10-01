package io.github.raggedycoder.flowable.binding.widget

import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.AdapterView.INVALID_POSITION
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun <T : Adapter> AdapterView<T>.itemSelections(skipInitial: Boolean = false) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial) offer(selectedItemPosition)
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (isActive) offer(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            if (isActive) offer(INVALID_POSITION)
        }
    }
}
