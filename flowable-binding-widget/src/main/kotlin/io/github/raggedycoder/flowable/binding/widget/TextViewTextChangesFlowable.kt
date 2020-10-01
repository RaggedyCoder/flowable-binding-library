package io.github.raggedycoder.flowable.binding.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

data class TextViewOnTextChangeEvent(
    val text: CharSequence?,
    val start: Int,
    val before: Int,
    val count: Int
)

@ExperimentalCoroutinesApi
fun TextView.textChanges() = callbackFlow<TextViewOnTextChangeEvent> {
    if (!isInMainThread) return@callbackFlow
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (isActive) offer(TextViewOnTextChangeEvent(s, start, before, count))
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }
    addTextChangedListener(textWatcher)
    awaitClose { removeTextChangedListener(textWatcher) }
}
