package com.example.restaurantsearch

import android.content.Context
import com.example.restaurantsearch.network.RetroFitClient
import com.example.restaurantsearch.sort_comparators.RestaruantPriceComparator
import com.example.restaurantsearch.sort_comparators.RestaruantRatingComparator
import com.google.gson.Gson
import io.reactivex.Observable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class RestaurantIntaractor {

    fun getRestaurantsTest(query: String, context: Context): Observable<List<RestaurantItem>> {
        return Observable.create<List<RestaurantItem>> {
            val restaurantModel: RestaurantModel = Gson().fromJson(context.assets.open("test.json").bufferedReader().use { it.readText() }, RestaurantModel::class.java)
            it.onNext(restaurantModel.restaurantList!!)
        }
    }

    fun getRestaurants(query: String, context: Context): Observable<List<RestaurantItem>> {
         return RetroFitClient.getRetroFitService(context).getRestaurants(query)
             .map { it.restaurantList }
    }

    fun getResettedRestaurants(list: List<RestaurantItem>): Observable<List<RestaurantItem>> {
        return Observable.create<List<RestaurantItem>> {
            val resetted = list.toMutableList()
            it.onNext(resetted)
        }
    }

    fun getRatingFilteredRestaurants(list: List<RestaurantItem>): Observable<List<RestaurantItem>> {
        return Observable.create<List<RestaurantItem>> {
            val sortedList = list.toMutableList()
            Collections.sort(sortedList, RestaruantRatingComparator())

            it.onNext(sortedList)
        }
    }

    fun getPriceFilteredRestaurants(list: List<RestaurantItem>): Observable<List<RestaurantItem>> {
        return Observable.create<List<RestaurantItem>> {
            val sortedList = list.toMutableList()
            Collections.sort(sortedList, RestaruantPriceComparator())

            it.onNext(sortedList)
        }
    }

    fun getCuisineGroupedRestaurants(list: List<RestaurantItem>?): Observable<List<GroupedRestaurants>> {
        return Observable.create<List<GroupedRestaurants>> {
            val groupedList = ArrayList<GroupedRestaurants>()

            val cuisines = HashSet<String>()
            for (item in list ?: arrayListOf()) {
                item.restaurant?.cuisines?.apply {
                    cuisines.add(this)
                }
            }

            for (cuisine in cuisines) {
                val restaurantList = ArrayList<Restaurant>()
                for (item in list ?: arrayListOf()) {
                    if (item.restaurant?.cuisines.equals(cuisine)) {
                        restaurantList.add(item.restaurant!!)
                    }
                }

                groupedList.add(GroupedRestaurants(cuisine, restaurantList))
            }

            it.onNext(groupedList)
        }
    }




}