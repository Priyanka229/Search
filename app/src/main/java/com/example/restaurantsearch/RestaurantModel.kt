package com.example.restaurantsearch

import com.google.gson.annotations.SerializedName

data class RestaurantModel(
    @SerializedName("restaurants") val restaurantList: List<RestaurantItem>?
)

data class RestaurantItem(
    @SerializedName("restaurant") val restaurant: Restaurant?
)

data class Restaurant(
    @SerializedName("name") val name: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("thumb") val thumb: String?,
    @SerializedName("cuisines") val cuisines: String?,
    @SerializedName("average_cost_for_two") val cost: String?,
    @SerializedName("user_rating") val userRating: UserRating?
)

data class UserRating(
    @SerializedName("aggregate_rating") val rating: String?
)

data class GroupedRestaurants(val cuisine: String, val restaurantList: List<Restaurant>)