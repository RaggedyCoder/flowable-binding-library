package io.github.raggedycoder.flowable.binding.leanback

import androidx.leanback.widget.SearchBar
import io.github.raggedycoder.flowable.binding.isInMainThread
import io.github.raggedycoder.flowable.binding.leanback.SearchBarSearchQueryEvent.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive

sealed class SearchBarSearchQueryEvent {
    abstract val searchQuery: String

    data class SearchBarSearchQueryChangedEvent(
        override val searchQuery: String
    ) : SearchBarSearchQueryEvent()

    data class SearchBarSearchQueryKeyboardDismissedEvent(
        override val searchQuery: String
    ) : SearchBarSearchQueryEvent()

    data class SearchBarSearchQuerySubmittedEvent(
        override val searchQuery: String
    ) : SearchBarSearchQueryEvent()
}

@ExperimentalCoroutinesApi
fun SearchBar.searchQueryChanges() = searchQueryChangeEvents()
    .filter { it is SearchBarSearchQueryChangedEvent }
    .map { it.searchQuery }

@ExperimentalCoroutinesApi
fun SearchBar.keyboardDismisses() = searchQueryChangeEvents()
    .filter { it is SearchBarSearchQueryKeyboardDismissedEvent }
    .map { it.searchQuery }

@ExperimentalCoroutinesApi
fun SearchBar.searchQuerySubmits() = searchQueryChangeEvents()
    .filter { it is SearchBarSearchQuerySubmittedEvent }
    .map { it.searchQuery }

@ExperimentalCoroutinesApi
fun SearchBar.searchQueryChangeEvents() = callbackFlow<SearchBarSearchQueryEvent> {
    if (!isInMainThread) return@callbackFlow
    setSearchBarListener(object : SearchBar.SearchBarListener {
        override fun onSearchQueryChange(query: String) {
            sendEvent(SearchBarSearchQueryChangedEvent(query))
        }

        override fun onSearchQuerySubmit(query: String) {
            sendEvent(SearchBarSearchQuerySubmittedEvent(query))
        }

        override fun onKeyboardDismiss(query: String) {
            sendEvent(SearchBarSearchQueryKeyboardDismissedEvent(query))
        }

        private fun sendEvent(event: SearchBarSearchQueryEvent) {
            if (isActive) offer(event)
        }
    })
    awaitClose { setSearchBarListener(null) }
}
