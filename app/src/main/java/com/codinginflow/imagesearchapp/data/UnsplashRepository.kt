package com.codinginflow.imagesearchapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.codinginflow.imagesearchapp.api.UnsplashApi
import javax.inject.Inject
import javax.inject.Singleton

//The class constructor has an UnsplashApi object which will be injected by Dagger
// @Inject constructor tells Dagger how to create UnsplashRepository and
// forward an UnsplashApi instance to it when instantiated
// For classes that we own, we use @Inject constructor rather than @Provides
// @Provides is used for classes that we don't own
@Singleton
class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi) {

    //The data from the PagingSource
    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20, // Number of items per Page
                maxSize = 100, //Fetch 100 more items in advance. Only have 100 items in the recyclerView
                enablePlaceholders = false
            ),
            pagingSourceFactory = {UnsplashPagingSource(unsplashApi,  query)}
        ).liveData //Convert the page data to liveData that can be observed
}