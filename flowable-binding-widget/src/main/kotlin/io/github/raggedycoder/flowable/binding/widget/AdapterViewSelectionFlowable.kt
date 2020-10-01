package io.github.raggedycoder.flowable.binding.widget

import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.AdapterView.INVALID_POSITION
import io.github.raggedycoder.flowable.binding.isInMainThread
import io.github.raggedycoder.flowable.binding.widget.AdapterViewSelectionEvent
.AdapterViewItemSelectionEvent
import io.github.raggedycoder.flowable.binding.widget.AdapterViewSelectionEvent
.AdapterViewNothingSelectionEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

sealed class AdapterViewSelectionEvent {
    data class AdapterViewItemSelectionEvent(
        val position: Int,
        val id: Long
    ) : AdapterViewSelectionEvent()

    object AdapterViewNothingSelectionEvent : AdapterViewSelectionEvent()
}

@ExperimentalCoroutinesApi
fun <T : Adapter> AdapterView<T>.selectionEvents(skipInitial: Boolean = false) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial) {
        if (selectedItemPosition == INVALID_POSITION) {
            offer(AdapterViewNothingSelectionEvent)
        } else {
            offer(AdapterViewItemSelectionEvent(selectedItemPosition, selectedItemId))
        }
    }
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (isActive) offer(AdapterViewItemSelectionEvent(position, id))
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            if (isActive) offer(AdapterViewNothingSelectionEvent)
        }

    }
}
