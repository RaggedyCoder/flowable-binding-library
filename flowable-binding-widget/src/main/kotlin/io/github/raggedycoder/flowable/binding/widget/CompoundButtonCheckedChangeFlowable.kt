package io.github.raggedycoder.flowable.binding.widget

import android.widget.CompoundButton
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun CompoundButton.checkChanges(skipInitial: Boolean = false) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial) offer(isChecked)
    setOnCheckedChangeListener { _, isChecked -> if (isActive) offer(isChecked) }
    awaitClose { setOnCheckedChangeListener(null) }
}
