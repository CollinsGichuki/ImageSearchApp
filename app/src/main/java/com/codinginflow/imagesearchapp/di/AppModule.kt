package com.codinginflow.imagesearchapp.di

import com.codinginflow.imagesearchapp.api.UnsplashApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module //Turns this into a Dagger Module
@InstallIn(ApplicationComponent::class) //Create an instance of this object and scope it to the application lifecycle
object AppModule {
    //Returns a Retrofit instance
    //@Provides turns the method to a Dagger Provide function
    // The function tells Dagger how to create a Retrofit Object
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        //Instantiate a Retrofit Object
        Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    //We need a Retrofit instance to create the UnsplashApi instance
    //Dagger already knows how to create a Retrofit instance hence we can pass it as a method parameter.
    @Provides
    @Singleton
    fun provideUnSplashApi(retrofit: Retrofit): UnsplashApi =
        retrofit.create(UnsplashApi::class.java)

}