package io.github.raggedycoder.flowable.binding.widget

import android.widget.Toolbar
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun Toolbar.navigationClicks() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    setNavigationOnClickListener { if (isActive) offer(Unit) }
    awaitClose { setNavigationOnClickListener(null) }
}
