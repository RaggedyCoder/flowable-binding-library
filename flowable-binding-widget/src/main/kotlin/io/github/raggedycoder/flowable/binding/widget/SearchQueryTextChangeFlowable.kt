package io.github.raggedycoder.flowable.binding.widget

import android.widget.SearchView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun SearchView.queryTextChanges(skipInitial: Boolean = false) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial && query != null) offer(query!!)
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            if (isActive) offer(query)
            return isActive
        }

        override fun onQueryTextChange(newText: String?) = false

    })

    awaitClose { setOnQueryTextListener(null) }
}
