package io.github.raggedycoder.flowable.binding.material

import com.google.android.material.chip.Chip
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun Chip.closeIconClicks() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    setOnCloseIconClickListener { if (isActive) offer(Unit) }
    awaitClose { setOnCloseIconClickListener(null) }
}
