package io.github.raggedycoder.flowable.binding.core

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import io.github.raggedycoder.flowable.binding.isInMainThread
import io.github.raggedycoder.flowable.binding.view.ViewScrollChangeEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@RequiresApi(Build.VERSION_CODES.M)
@ExperimentalCoroutinesApi
fun NestedScrollView.scrollChangeEvents() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    setOnScrollChangeListener { _: NestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
        if (isActive) offer(ViewScrollChangeEvent(scrollX, scrollY, oldScrollX, oldScrollY))
    }
    awaitClose { setOnScrollChangeListener(null as OnScrollChangeListener?) }
}
