package com.example.restaurantsearch.sort_comparators

import com.example.restaurantsearch.RestaurantItem

class RestaruantRatingComparator: Comparator<RestaurantItem>
{
    override fun compare(r1: RestaurantItem, r2: RestaurantItem): Int {
        val rating1 = r1.restaurant?.userRating?.rating!!.toFloat()
        val rating2 = r2.restaurant?.userRating?.rating!!.toFloat()

        return when {
            rating1 < rating2 -> 1
            rating1 > rating2 -> -1
            else -> 0
        }
    }

}
