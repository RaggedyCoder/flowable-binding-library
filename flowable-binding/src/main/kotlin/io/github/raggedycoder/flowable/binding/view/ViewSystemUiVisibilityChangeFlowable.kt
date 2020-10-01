package io.github.raggedycoder.flowable.binding.view

import android.view.View
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun View.systemUiVisibilityChanges() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    setOnSystemUiVisibilityChangeListener { if (isActive) offer(it) }
    awaitClose { setOnSystemUiVisibilityChangeListener(null) }
}
