package com.example.restaurantsearch.sort_comparators

import com.example.restaurantsearch.RestaurantItem

class RestaruantPriceComparator: Comparator<RestaurantItem>
{
    override fun compare(r1: RestaurantItem, r2: RestaurantItem): Int {
        val cost1 = r1.restaurant?.cost!!.toFloat()
        val cost2 = r2.restaurant?.cost!!.toFloat()

        return when {
            cost1 < cost2 -> -1
            cost1 > cost2 -> 1
            else -> 0
        }
    }

}
