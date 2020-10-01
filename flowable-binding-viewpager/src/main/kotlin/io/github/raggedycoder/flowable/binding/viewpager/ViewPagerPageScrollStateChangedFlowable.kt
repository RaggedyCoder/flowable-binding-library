package io.github.raggedycoder.flowable.binding.viewpager

import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun ViewPager.pageScrollStateChanges() = callbackFlow<Int> {
    if (!isInMainThread) return@callbackFlow
    val listener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {
        }

        override fun onPageScrollStateChanged(state: Int) {
            if (isActive) offer(state)
        }
    }
    addOnPageChangeListener(listener)
    awaitClose { removeOnPageChangeListener(listener) }
}
