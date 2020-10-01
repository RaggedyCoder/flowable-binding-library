package io.github.raggedycoder.flowable.binding.material

import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun BottomNavigationView.itemSelections(
    skipInitial: Boolean = false,
    handled: (MenuItem) -> Boolean
) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial) {
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            if (menuItem.isChecked) {
                offer(menuItem)
                break
            }
        }
    }
    setOnNavigationItemSelectedListener {
        if (isActive) {
            try {
                if (handled(it)) {
                    offer(it)
                    return@setOnNavigationItemSelectedListener true
                }
            } catch (ex: Exception) {
                cancel(ex.message ?: "", ex)
            }
        }
        return@setOnNavigationItemSelectedListener false
    }
    awaitClose { setOnNavigationItemSelectedListener(null) }
}
