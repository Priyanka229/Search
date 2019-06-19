package com.example.restaurantsearch

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import io.reactivex.subjects.PublishSubject


class MainActivity : AppCompatActivity() {

    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerview) }
    private val noDataTv by lazy { findViewById<TextView>(R.id.no_data_tv) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }

    private val viewModel by lazy { ViewModelProviders.of(this).get(RestaurantVM::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val restaurantAdapter = RestaurantAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = restaurantAdapter
        }


        viewModel.liveData.observe(this, Observer {
            it?.apply {
                progressBar.visibility = View.GONE

                if (it.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    noDataTv.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    noDataTv.visibility = View.GONE

                    restaurantAdapter.restaurantList = this
                    restaurantAdapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.restaurant_menu, menu)

        menu?.let {
            val searchItem = it.findItem(R.id.restaurant_search)
            val searchView = searchItem?.actionView as SearchView
            searchView.apply {
                queryHint = "type here"
                setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        noDataTv.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE

                        viewModel.onQuerySubmit(p0)
                        return false
                    }
                })
            }


        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.reset -> {
                viewModel.resetFilters()
                true
            }
            R.id.restaurant_cost_sort -> {
                viewModel.priceSortApply()
                true
            }
            R.id.restaurant_rating_sort -> {
                viewModel.ratingSortApply()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
