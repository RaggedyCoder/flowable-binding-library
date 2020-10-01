package io.github.raggedycoder.flowable.binding.slidingpanelayout

import android.view.View
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun SlidingPaneLayout.panelSlides() = callbackFlow<Float> {
    if (!isInMainThread) return@callbackFlow
    setPanelSlideListener(object : SlidingPaneLayout.PanelSlideListener {
        override fun onPanelSlide(panel: View, slideOffset: Float) {
            if (isActive) offer(slideOffset)
        }

        override fun onPanelOpened(panel: View) {
        }

        override fun onPanelClosed(panel: View) {
        }
    })
    awaitClose { setPanelSlideListener(null) }
}
