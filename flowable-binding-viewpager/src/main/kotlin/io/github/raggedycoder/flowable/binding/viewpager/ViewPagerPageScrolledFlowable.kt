package io.github.raggedycoder.flowable.binding.viewpager

import androidx.viewpager.widget.ViewPager
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
fun ViewPager.pageScrollEvents() = callbackFlow<ViewPagerPageScrollEvent> {
    if (!isInMainThread) return@callbackFlow
    val listener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            if (isActive)
                offer(ViewPagerPageScrollEvent(position, positionOffset, positionOffsetPixels))
        }

        override fun onPageSelected(position: Int) {
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }
    addOnPageChangeListener(listener)
    awaitClose { removeOnPageChangeListener(listener) }
}
