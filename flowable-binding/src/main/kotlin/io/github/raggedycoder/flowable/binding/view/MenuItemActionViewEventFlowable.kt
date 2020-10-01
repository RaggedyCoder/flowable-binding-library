package io.github.raggedycoder.flowable.binding.view

import android.view.MenuItem
import io.github.raggedycoder.flowable.binding.isInMainThread
import io.github.raggedycoder.flowable.binding.view.MenuItemActionViewEvent
.MenuItemActionViewCollapseEvent
import io.github.raggedycoder.flowable.binding.view.MenuItemActionViewEvent
.MenuItemActionViewExpandEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

sealed class MenuItemActionViewEvent {
    abstract val menuItem: MenuItem

    data class MenuItemActionViewExpandEvent(override val menuItem: MenuItem) :
        MenuItemActionViewEvent()

    data class MenuItemActionViewCollapseEvent(override val menuItem: MenuItem) :
        MenuItemActionViewEvent()
}

@ExperimentalCoroutinesApi
fun MenuItem.actionViewEvents(
    handled: (MenuItemActionViewEvent) -> Boolean
) = callbackFlow<MenuItemActionViewEvent> {
    if (!isInMainThread) return@callbackFlow
    setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
        override fun onMenuItemActionExpand(item: MenuItem) =
            onEvent(MenuItemActionViewExpandEvent(item))

        override fun onMenuItemActionCollapse(item: MenuItem) =
            onEvent(MenuItemActionViewCollapseEvent(item))

        private fun onEvent(menuItemActionViewEvent: MenuItemActionViewEvent): Boolean {
            if (isActive) {
                try {
                    if (handled(menuItemActionViewEvent)) {
                        offer(menuItemActionViewEvent)
                        return true
                    }
                } catch (ex: Exception) {
                    cancel(ex.message ?: "", ex)
                }
            }
            return false
        }
    })
    awaitClose { setOnActionExpandListener(null) }
}
