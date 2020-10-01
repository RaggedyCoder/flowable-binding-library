package io.github.raggedycoder.flowable.binding.drawerlayout

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.*
import io.github.raggedycoder.flowable.binding.drawerlayout.DrawerState.*
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

enum class DrawerState {
    IDLE, DRAGGING, SETTLING, UNKNOWN
}

@ExperimentalCoroutinesApi
fun DrawerLayout.drawerStateChanges() = callbackFlow<DrawerState> {
    if (!isInMainThread) return@callbackFlow

    val listener = object : DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

        }

        override fun onDrawerOpened(drawerView: View) {

        }

        override fun onDrawerClosed(drawerView: View) {

        }

        override fun onDrawerStateChanged(newState: Int) {
            if (isActive) {
                when (newState) {
                    STATE_IDLE -> offer(IDLE)
                    STATE_DRAGGING -> offer(DRAGGING)
                    STATE_SETTLING -> offer(SETTLING)
                    else -> offer(UNKNOWN)
                }
            }
        }

    }
    addDrawerListener(listener)
    awaitClose { removeDrawerListener(listener) }
}
