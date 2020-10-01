package io.github.raggedycoder.flowable.binding.viewpager2

import androidx.viewpager2.widget.ViewPager2
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

data class ViewPagerPageScrollEvent(
    val position: Int,
    val positionOffset: Float,
    val positionOffsetPixels: Int
)

@ExperimentalCoroutinesApi
fun ViewPager2.pageScrollEvents() = callbackFlow<ViewPagerPageScrollEvent> {
    if (!isInMainThread) return@callbackFlow
    val listener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            if (isActive)
                offer(ViewPagerPageScrollEvent(position, positionOffset, positionOffsetPixels))
        }
    }
    registerOnPageChangeCallback(listener)
    awaitClose { unregisterOnPageChangeCallback(listener) }
}
