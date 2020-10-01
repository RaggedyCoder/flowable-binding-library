package io.github.raggedycoder.flowable.binding.viewpager2

import androidx.viewpager2.widget.ViewPager2
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun ViewPager2.pageSelections() = callbackFlow<Int> {
    if (!isInMainThread) return@callbackFlow
    val listener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (isActive) offer(position)
        }
    }
    registerOnPageChangeCallback(listener)
    awaitClose { unregisterOnPageChangeCallback(listener) }
}
