package io.github.raggedycoder.flowable.binding.swiperefreshlayout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@RequiresApi(Build.VERSION_CODES.M)
@ExperimentalCoroutinesApi
fun SwipeRefreshLayout.refreshes() = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    setOnRefreshListener { if (isActive) offer(Unit) }
    awaitClose { setOnRefreshListener(null) }
}
