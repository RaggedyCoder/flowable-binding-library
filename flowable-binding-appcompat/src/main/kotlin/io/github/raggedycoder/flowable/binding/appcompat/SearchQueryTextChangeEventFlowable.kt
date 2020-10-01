package io.github.raggedycoder.flowable.binding.appcompat


import androidx.appcompat.widget.SearchView
import io.github.raggedycoder.flowable.binding.isInMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

data class SearchQueryTextChangeEvent(
    val queryText: CharSequence,
    val isSubmitted: Boolean
)

@ExperimentalCoroutinesApi
fun SearchView.queryTextChangeEvents(skipInitial: Boolean = false) = callbackFlow {
    if (!isInMainThread) return@callbackFlow
    if (isActive && !skipInitial) offer(SearchQueryTextChangeEvent(query, false))
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            if (isActive) offer(SearchQueryTextChangeEvent(query, true))
            return isActive
        }

        override fun onQueryTextChange(newText: String): Boolean {
            if (isActive) offer(SearchQueryTextChangeEvent(newText, false))
            return isActive
        }

    })

    awaitClose { setOnQueryTextListener(null) }
}
