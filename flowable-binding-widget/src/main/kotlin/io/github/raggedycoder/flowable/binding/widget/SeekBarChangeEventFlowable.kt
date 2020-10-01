package io.github.raggedycoder.flowable.binding.widget

import android.widget.SeekBar
import io.github.raggedycoder.flowable.binding.isInMainThread
import io.github.raggedycoder.flowable.binding.widget.SeekBarChangeEvent.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

sealed class SeekBarChangeEvent {
    data class SeekBarProgressChangeEvent(
        val progress: Int,
        val fromUser: Boolean
    ) : SeekBarChangeEvent()

    object SeekBarStartChangeEvent : SeekBarChangeEvent()
    object SeekBarStopChangeEvent : SeekBarChangeEvent()
}

@ExperimentalCoroutinesApi
fun SeekBar.changeEvents(skipInitial: Boolean = false) = callbackFlow<SeekBarChangeEvent> {
    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial) offer(SeekBarProgressChangeEvent(progress, false))
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) =
            sendEvent(SeekBarProgressChangeEvent(progress, fromUser))

        override fun onStartTrackingTouch(seekBar: SeekBar?) =
            sendEvent(SeekBarStartChangeEvent)

        override fun onStopTrackingTouch(seekBar: SeekBar?) =
            sendEvent(SeekBarStopChangeEvent)

        private fun sendEvent(seekBarChangeEvent: SeekBarChangeEvent) {
            if (isActive) offer(seekBarChangeEvent)
        }
    })

    awaitClose { setOnSeekBarChangeListener(null) }
}
