package io.github.raggedycoder.flowable.binding.view

import android.view.View
import android.view.View.OnLayoutChangeListener
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

data class ViewLayoutChangeEvent(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val oldLeft: Int,
    val oldTop: Int,
    val oldRight: Int,
    val oldBottom: Int
)

@ExperimentalCoroutinesApi
fun View.layoutChangeEvents() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    val listener =
        OnLayoutChangeListener { _, left, top, right, bottom, oLeft, oTop, oRight, oBottom ->
            if (isActive)
                offer(
                    ViewLayoutChangeEvent(
                        left,
                        top,
                        right,
                        bottom,
                        oLeft,
                        oTop,
                        oRight,
                        oBottom
                    )
                )
        }
    addOnLayoutChangeListener(listener)
    awaitClose { removeOnLayoutChangeListener(listener) }
}
