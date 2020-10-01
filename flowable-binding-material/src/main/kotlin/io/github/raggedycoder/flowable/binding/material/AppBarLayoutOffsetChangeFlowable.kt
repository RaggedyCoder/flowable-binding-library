package io.github.raggedycoder.flowable.binding.material

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun AppBarLayout.offsetChanges() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    val listener = OnOffsetChangedListener { _, offset -> if (isActive) offer(offset) }
    addOnOffsetChangedListener(listener)
    awaitClose { removeOnOffsetChangedListener(listener) }
}
