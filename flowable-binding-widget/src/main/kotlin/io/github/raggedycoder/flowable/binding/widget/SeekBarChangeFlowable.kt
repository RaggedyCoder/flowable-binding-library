package io.github.raggedycoder.flowable.binding.widget

import android.widget.SeekBar
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun SeekBar.userChanges(skipInitial: Boolean = false) = changes(true, skipInitial)

@ExperimentalCoroutinesApi
fun SeekBar.systemChanges(skipInitial: Boolean = false) = changes(false, skipInitial)

@ExperimentalCoroutinesApi
private fun SeekBar.changes(
    userChangeOnly: Boolean,
    skipInitial: Boolean = false
) = callbackFlow {

    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial) offer(progress)
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (isActive && userChangeOnly == fromUser) offer(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    })

    awaitClose { setOnSeekBarChangeListener(null) }
}
