package com.codinginflow.imagesearchapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//Use Parcelable to make it easier to transfer the data between the activities
@Parcelize
data class UnsplashPhoto(
    val id: String,
    val description: String?,
    val user: UnsplashPhotoUser,
    val urls: UnsplashPhotoUrls,
) : Parcelable {

    @Parcelize
    data class UnsplashPhotoUser(
        val name: String,
        val username: String,
    ) : Parcelable {
        val attributionUrl get() = "https://unsplash.com/$username?utm_source=ImageSearchApp&utm_medium=referral"
    }

    @Parcelize
    data class UnsplashPhotoUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String,
    ) : Parcelable

}