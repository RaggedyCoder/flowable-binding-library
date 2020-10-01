package io.github.raggedycoder.flowable.binding.drawerlayout

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun DrawerLayout.drawerOpen(gravity: Int, skipInitial: Boolean = false) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial) offer(isDrawerOpen(gravity))

    val listener = object : DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

        }

        override fun onDrawerOpened(drawerView: View) {
            if (isActive) {
                val drawerGravity =
                    (drawerView.layoutParams as? DrawerLayout.LayoutParams)?.gravity ?: return
                if (gravity == drawerGravity) offer(true)
            }
        }

        override fun onDrawerClosed(drawerView: View) {
            if (isActive) {
                val drawerGravity =
                    (drawerView.layoutParams as? DrawerLayout.LayoutParams)?.gravity ?: return
                if (gravity == drawerGravity) offer(false)
            }
        }

        override fun onDrawerStateChanged(newState: Int) {
        }

    }
    addDrawerListener(listener)
    awaitClose { removeDrawerListener(listener) }
}
