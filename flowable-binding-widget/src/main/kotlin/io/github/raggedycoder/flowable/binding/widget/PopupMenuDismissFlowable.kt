package io.github.raggedycoder.flowable.binding.widget

import android.widget.PopupMenu
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun PopupMenu.dismisses() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    setOnDismissListener { if (isActive) offer(Unit) }
    awaitClose { setOnDismissListener(null) }
}
