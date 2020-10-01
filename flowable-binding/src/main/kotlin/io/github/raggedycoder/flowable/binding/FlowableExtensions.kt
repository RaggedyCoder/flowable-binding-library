package io.github.raggedycoder.flowable.binding

import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

val CoroutineScope.isInMainThread: Boolean
    get() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            cancel(
                "Expected to be called on the main thread but was ${Thread.currentThread().name}"
            )
            return false
        }
        return true
    }
