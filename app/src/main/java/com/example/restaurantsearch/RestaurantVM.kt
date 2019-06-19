package com.example.restaurantsearch

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class RestaurantVM(application: Application): AndroidViewModel(application) {
    enum class Filter(filter: Int) {
        RESET(0), PRICE(1), RATING(2)
    }

    private var currentFilter = Filter.RESET
    private var rawList: List<RestaurantItem>? = null
    private val restaurantIntaractor: RestaurantIntaractor = RestaurantIntaractor()
    val liveData: MutableLiveData<List<GroupedRestaurants>> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
//    private var searchQuerySubject = PublishSubject.create<String>()

    fun start() {
        liveData.value = listOf()
//        getDataFromIntaractor()
    }

    fun getDataFromIntaractor(query: String) {
        compositeDisposable.add(
//            searchQuerySubject
//                .debounce(300, TimeUnit.MILLISECONDS)
//                .flatMap {query ->  restaurantIntaractor.getRestaurants(query, getApplication()) }
            restaurantIntaractor.getRestaurants(query, getApplication())
                .flatMap {
                    rawList = it

                    when (currentFilter) {
                        Filter.PRICE -> restaurantIntaractor.getRatingFilteredRestaurants(it)
                        Filter.RATING -> restaurantIntaractor.getRatingFilteredRestaurants(it)
                        else ->  restaurantIntaractor.getResettedRestaurants(it)
                    }
                }
                .flatMap {
                    restaurantIntaractor.getCuisineGroupedRestaurants(it)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    {
                        liveData.value = it
                    },
                    {
                        liveData.value = listOf()
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun onQuerySubmit(query: String?) {
        query?.let {
            if (it.isNotEmpty()) {
//                searchQuerySubject = PublishSubject.create<String>()
//                searchQuerySubject.onNext(it)
                getDataFromIntaractor(query)
            } else {
                liveData.value = listOf()
            }
        }
    }

    fun resetFilters() {
        currentFilter = Filter.RESET

        rawList?.apply {
            restaurantIntaractor.getResettedRestaurants(this)
                .flatMap { restaurantIntaractor.getCuisineGroupedRestaurants(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    liveData.value = it
                }
        }
    }

    fun priceSortApply() {
        currentFilter = Filter.PRICE

        rawList?.apply {
            restaurantIntaractor.getPriceFilteredRestaurants(this)
                .flatMap { restaurantIntaractor.getCuisineGroupedRestaurants(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    liveData.value = it
                }
        }
    }

    fun ratingSortApply() {
        currentFilter = Filter.RATING

        rawList?.apply {
            restaurantIntaractor.getRatingFilteredRestaurants(this)
                .flatMap { restaurantIntaractor.getCuisineGroupedRestaurants(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    liveData.value = it
                }
        }
    }
}