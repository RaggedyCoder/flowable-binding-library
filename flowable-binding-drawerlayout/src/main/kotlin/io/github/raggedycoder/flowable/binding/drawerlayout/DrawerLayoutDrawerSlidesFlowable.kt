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
fun DrawerLayout.drawerSlides(gravity: Int) = callbackFlow<Float> {
    if (!isInMainThread) return@callbackFlow

    val listener = object : DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            if (isActive) {
                val drawerGravity =
                    (drawerView.layoutParams as? DrawerLayout.LayoutParams)?.gravity ?: return
                if (gravity == drawerGravity) offer(slideOffset)
            }
        }

        override fun onDrawerOpened(drawerView: View) {

        }

        override fun onDrawerClosed(drawerView: View) {

        }

        override fun onDrawerStateChanged(newState: Int) {
        }
    }
    addDrawerListener(listener)
    awaitClose { removeDrawerListener(listener) }
}
