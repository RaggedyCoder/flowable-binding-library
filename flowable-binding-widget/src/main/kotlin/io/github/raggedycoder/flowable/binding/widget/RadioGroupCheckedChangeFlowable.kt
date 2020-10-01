package io.github.raggedycoder.flowable.binding.widget

import android.widget.RadioGroup
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun RadioGroup.checkChanges(skipInitial: Boolean = false) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial) offer(checkedRadioButtonId)
    setOnCheckedChangeListener { _, checkedId -> if (isActive) offer(checkedId) }
    awaitClose { setOnCheckedChangeListener(null) }
}
