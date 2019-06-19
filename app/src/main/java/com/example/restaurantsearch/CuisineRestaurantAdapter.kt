package com.example.restaurantsearch

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class CuisineRestaurantAdapter: RecyclerView.Adapter<CuisineRestaurantAdapter.CuisineRestaurantViewHolder>() {

    var restaurantList: List<Restaurant>? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CuisineRestaurantViewHolder {
        return CuisineRestaurantViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.horizontal_recyclerview_item, null))
    }

    override fun getItemCount(): Int {
        return restaurantList?.size ?: 0
    }

    override fun onBindViewHolder(p0: CuisineRestaurantViewHolder, p1: Int) {
        p0.bind(restaurantList!![p1])
    }

    class CuisineRestaurantViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val restaurantName = view.findViewById<TextView>(R.id.restaurant_name)
        val restaurantRating = view.findViewById<TextView>(R.id.restaurant_rating)
        val restaurantCost = view.findViewById<TextView>(R.id.restaurant_cost)
        val restaurantIv = view.findViewById<ImageView>(R.id.image_iv)

        fun bind(item: Restaurant) {

            val rating = item.userRating?.rating + "*"
            val cost = item.cost + "Rs"
            restaurantName.text = item.name
            restaurantRating.text = rating
            restaurantCost.text = cost
            Glide
                .with(view.context)
                .load(item.thumb)
                .placeholder(R.drawable.no_image)
                .centerCrop()
                .into(restaurantIv)

        }
    }
}