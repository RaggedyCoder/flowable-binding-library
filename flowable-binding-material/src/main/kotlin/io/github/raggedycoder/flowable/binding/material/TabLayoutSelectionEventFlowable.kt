package io.github.raggedycoder.flowable.binding.material

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import io.github.raggedycoder.flowable.binding.isInMainThread
import io.github.raggedycoder.flowable.binding.material.TabLayoutSelectionEvent.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive


sealed class TabLayoutSelectionEvent {
    abstract val tab: Tab

    data class TabLayoutSelectionSelectedEvent(
        override val tab: Tab
    ) : TabLayoutSelectionEvent()

    data class TabLayoutSelectionReselectedEvent(
        override val tab: Tab
    ) : TabLayoutSelectionEvent()

    data class TabLayoutSelectionUnselectedEvent(
        override val tab: Tab
    ) : TabLayoutSelectionEvent()
}

@ExperimentalCoroutinesApi
fun TabLayout.selectionEvents(skipInitial: Boolean = false) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial) {
        if (selectedTabPosition != -1) {
            offer(TabLayoutSelectionSelectedEvent(getTabAt(selectedTabPosition)!!))
        }
    }
    val listener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: Tab) {
            sendEvent(TabLayoutSelectionSelectedEvent(tab))
        }

        override fun onTabUnselected(tab: Tab) {
            sendEvent(TabLayoutSelectionUnselectedEvent(tab))
        }

        override fun onTabReselected(tab: Tab) {
            sendEvent(TabLayoutSelectionReselectedEvent(tab))
        }

        private fun sendEvent(event: TabLayoutSelectionEvent) {
            if (isActive) offer(event)
        }
    }

    awaitClose { removeOnTabSelectedListener(listener) }
}
