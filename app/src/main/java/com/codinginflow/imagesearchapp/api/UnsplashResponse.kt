package com.codinginflow.imagesearchapp.api

import com.codinginflow.imagesearchapp.data.UnsplashPhoto

//This class contains the response from the Unsplash Api
data class UnsplashResponse(
    //The only response we want from the largest object
    var results: List<UnsplashPhoto>
)