package com.codinginflow.imagesearchapp.data

import androidx.paging.PagingSource
import com.codinginflow.imagesearchapp.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

//Class responsible for getting the data from the API and loading it in pages
class UnsplashPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
) : PagingSource<Int, UnsplashPhoto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        //triggers the api request and turns the data to pages
        //Holds the current page we are in
        val position = params.key
            ?: UNSPLASH_STARTING_PAGE_INDEX //If the current page is null, load the first page

        //Wrap the API call in a try catch
        return try {
            val response = unsplashApi.searchPhotos(query, position, params.loadSize)
            //Filtering the data we want from the response
            val photos = response.results

            //If Successful, return the loadPage
            LoadResult.Page(
                //Build the Page
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position -1,
                nextKey = if (photos.isEmpty()) null else position + 1
                )
        } catch (exception: IOException){
            //Say there is no internet connection when making the request
            LoadResult.Error(exception)
        } catch (exception: HttpException){
            //Something wrong happened in the server-side when making the request
            LoadResult.Error(exception)
        }

    }
}