package io.github.raggedycoder.flowable.binding.widget

import android.widget.RatingBar
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

data class RatingBarChangeEvent(val rating: Float, val fromUser: Boolean)

@ExperimentalCoroutinesApi
fun RatingBar.ratingChangeEvents(skipInitial: Boolean = false) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial) offer(RatingBarChangeEvent(rating, false))
    setOnRatingBarChangeListener { _, rating, fromUser ->
        if (isActive) offer(RatingBarChangeEvent(rating, fromUser))
    }

    awaitClose { onRatingBarChangeListener = null }
}
