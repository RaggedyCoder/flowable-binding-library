package io.github.raggedycoder.flowable.binding.widget

import android.view.KeyEvent
import android.widget.TextView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

data class TextViewEditorActionEvent(
    /** The view from which this event occurred.  */
    val actionId: Int,
    val keyEvent: KeyEvent?
)

@ExperimentalCoroutinesApi
fun TextView.editorActionEvents(handled: (TextViewEditorActionEvent) -> Boolean) =
    callbackFlow {
        if (!isInMainThread) return@callbackFlow
        setOnEditorActionListener { _, actionId, event ->
            val model = TextViewEditorActionEvent(actionId, event)
            if (handled(model)) {
                if (isActive) offer(model)
                true
            } else false
        }
        awaitClose { setOnEditorActionListener(null) }
    }
