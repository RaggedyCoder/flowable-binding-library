package io.github.raggedycoder.flowable.binding.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

data class TextViewBeforeTextChangeEvent(
    val text: CharSequence?,
    val start: Int,
    val count: Int,
    val after: Int
)

@ExperimentalCoroutinesApi
fun TextView.beforeTextChanges() = callbackFlow<TextViewBeforeTextChangeEvent> {
    if (!isInMainThread) return@callbackFlow
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            if (isActive) offer(TextViewBeforeTextChangeEvent(s, start, count, after))
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
        }
    }
    addTextChangedListener(textWatcher)
    awaitClose { removeTextChangedListener(textWatcher) }
}
