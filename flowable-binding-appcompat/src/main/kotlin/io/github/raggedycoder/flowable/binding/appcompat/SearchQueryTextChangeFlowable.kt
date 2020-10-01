package io.github.raggedycoder.flowable.binding.appcompat

import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun SearchView.queryTextChanges(skipInitial: Boolean = false) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial && query != null) offer(query!!)
    setOnQueryTextListener(object : OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            if (isActive) offer(query)
            return isActive
        }

        override fun onQueryTextChange(newText: String?) = false

    })

    awaitClose { setOnQueryTextListener(null) }
}
