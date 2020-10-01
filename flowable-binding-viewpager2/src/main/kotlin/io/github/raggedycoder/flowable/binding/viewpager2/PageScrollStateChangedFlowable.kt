package io.github.raggedycoder.flowable.binding.viewpager2

import androidx.viewpager2.widget.ViewPager2
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun ViewPager2.pageScrollStateChanges() = callbackFlow<Int> {
    if (!isInMainThread) return@callbackFlow
    val listener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            if (isActive) offer(state)
        }
    }
    registerOnPageChangeCallback(listener)
    awaitClose { unregisterOnPageChangeCallback(listener) }
}
