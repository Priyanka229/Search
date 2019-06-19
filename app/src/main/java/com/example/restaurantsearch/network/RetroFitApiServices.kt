package com.example.restaurantsearch.network

import com.example.restaurantsearch.RestaurantModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroFitApiServices {
    @GET("search")
    fun getRestaurants(@Query("q") query: String): Observable<RestaurantModel>
}