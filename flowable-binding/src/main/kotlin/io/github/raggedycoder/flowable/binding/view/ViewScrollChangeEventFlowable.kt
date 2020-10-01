package io.github.raggedycoder.flowable.binding.view

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

data class ViewScrollChangeEvent(
    val scrollX: Int,
    val scrollY: Int,
    val oldScrollX: Int,
    val oldScrollY: Int
)

@RequiresApi(Build.VERSION_CODES.M)
@ExperimentalCoroutinesApi
fun View.scrollChangeEvents() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    setOnScrollChangeListener { _, scrollX, scrollY, oldScrollX, oldScrollY ->
        if (isActive) offer(ViewScrollChangeEvent(scrollX, scrollY, oldScrollX, oldScrollY))
    }
    awaitClose { setOnScrollChangeListener(null) }
}
