package io.github.raggedycoder.flowable.binding.widget

import android.widget.AbsListView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

data class AbsListViewScrollEvent(
    val scrollState: Int,
    val firstVisibleItem: Int,
    val visibleItemCount: Int,
    val totalItemCount: Int
)

@ExperimentalCoroutinesApi
fun AbsListView.scrollEvents() = callbackFlow<AbsListViewScrollEvent> {
    if (!isInMainThread) return@callbackFlow
    setOnScrollListener(object : AbsListView.OnScrollListener {
        var currentScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE

        override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
            currentScrollState = scrollState
            if (isActive) offer(
                AbsListViewScrollEvent(
                    scrollState,
                    view.firstVisiblePosition,
                    view.childCount,
                    view.count
                )
            )
        }

        override fun onScroll(
            view: AbsListView,
            firstVisibleItem: Int,
            visibleItemCount: Int,
            totalItemCount: Int
        ) {
            if (isActive) offer(
                AbsListViewScrollEvent(
                    currentScrollState,
                    firstVisibleItem,
                    visibleItemCount,
                    totalItemCount
                )
            )
        }
    })

    awaitClose { setOnScrollListener(null) }
}
