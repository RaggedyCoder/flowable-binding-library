package io.github.raggedycoder.flowable.binding.widget

import android.widget.RatingBar
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun RatingBar.userRatingChanges(skipInitial: Boolean = false) = ratingChanges(true, skipInitial)

@ExperimentalCoroutinesApi
fun RatingBar.systemRatingChanges(skipInitial: Boolean = false) = ratingChanges(false, skipInitial)

@ExperimentalCoroutinesApi
private fun RatingBar.ratingChanges(
    userChangeOnly: Boolean,
    skipInitial: Boolean = false
) = callbackFlow {

    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial) offer(rating)
    setOnRatingBarChangeListener { _, rating, fromUser ->
        if (isActive && userChangeOnly == fromUser) offer(rating)
    }

    awaitClose { onRatingBarChangeListener = null  }
}
