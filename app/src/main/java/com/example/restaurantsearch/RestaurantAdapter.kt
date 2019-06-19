package com.example.restaurantsearch

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RestaurantAdapter: RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    var restaurantList: List<GroupedRestaurants>? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RestaurantViewHolder {
        return RestaurantViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.vertical_recyclerview_item, null))
    }

    override fun getItemCount(): Int {
        return restaurantList?.size ?: 0
    }

    override fun onBindViewHolder(p0: RestaurantViewHolder, p1: Int) {
        p0.bind(restaurantList!![p1])
    }

    class RestaurantViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val cuisineTitle = view.findViewById<TextView>(R.id.cuisine_tv)
        val recyclerView = view.findViewById<RecyclerView>(R.id.restaurant_rv)

        fun bind(groupedRestaurants: GroupedRestaurants) {
            cuisineTitle.text = groupedRestaurants.cuisine

//            recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
//            val adapter = CuisineRestaurantAdapter()
//            adapter.restaurantList = groupedRestaurants.restaurantList
//            recyclerView.adapter = adapter

            recyclerView.apply {
                layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
                this.adapter = CuisineRestaurantAdapter().apply {
                    restaurantList = groupedRestaurants.restaurantList
                }
            }
        }
    }
}