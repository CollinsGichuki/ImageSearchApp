package com.codinginflow.imagesearchapp.ui.gallery

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.codinginflow.imagesearchapp.data.UnsplashRepository

//Dagger will inject the ViewModel with a repository instance when the ViewModel is instantiated
class GalleryViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository,
    @Assisted state: SavedStateHandle // Saves data and retrieves them in process death scenarios
) : ViewModel() {

    // The last query wil be saved to the state
    // Load the last query from saved state
    // Or if none(first time launching the app), load the default query
    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    //SwitchMap updates the currentQuery value to use the latest value
    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)//Cache the result
    }

    //Called from the fragment
    fun searchPhotos(query: String) {
        //Equate value entered in the fragment with the value to be searched
        currentQuery.value = query
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "cats"
    }
}