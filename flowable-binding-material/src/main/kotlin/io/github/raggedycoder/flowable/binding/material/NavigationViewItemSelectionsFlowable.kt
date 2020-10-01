package io.github.raggedycoder.flowable.binding.material

import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun NavigationView.itemSelections(
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
    setNavigationItemSelectedListener {
        if (isActive) {
            try {
                if (handled(it)) {
                    offer(it)
                    return@setNavigationItemSelectedListener true
                }
            } catch (ex: Exception) {
                cancel(ex.message ?: "", ex)
            }
        }
        return@setNavigationItemSelectedListener false
    }
    awaitClose { setNavigationItemSelectedListener(null) }
}
