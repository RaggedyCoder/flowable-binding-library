package io.github.raggedycoder.flowable.binding.viewpager

import androidx.viewpager.widget.ViewPager
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun ViewPager.pageSelections() = callbackFlow<Int> {
    if (!isInMainThread) return@callbackFlow
    val listener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {
            if (isActive) offer(position)
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }
    addOnPageChangeListener(listener)
    awaitClose { removeOnPageChangeListener(listener) }
}

