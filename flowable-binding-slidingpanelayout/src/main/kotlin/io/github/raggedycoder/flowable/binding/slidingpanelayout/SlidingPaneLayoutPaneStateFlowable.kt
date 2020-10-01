package io.github.raggedycoder.flowable.binding.slidingpanelayout

import android.view.View
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

enum class PaneState {
    OPENED, CLOSED
}

@ExperimentalCoroutinesApi
fun SlidingPaneLayout.panelStates() = callbackFlow<PaneState> {
    if (!isInMainThread) return@callbackFlow
    setPanelSlideListener(object : SlidingPaneLayout.PanelSlideListener {
        override fun onPanelSlide(panel: View, slideOffset: Float) {

        }

        override fun onPanelOpened(panel: View) {
            if (isActive) offer(PaneState.OPENED)
        }

        override fun onPanelClosed(panel: View) {
            if (isActive) offer(PaneState.CLOSED)
        }
    })
    awaitClose { setPanelSlideListener(null) }
}
