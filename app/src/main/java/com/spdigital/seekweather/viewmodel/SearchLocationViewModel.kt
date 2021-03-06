package com.spdigital.seekweather.viewmodel

import androidx.lifecycle.MutableLiveData
import com.spdigital.seekweather.view.ListItemModel

interface SearchLocationViewModel {
    fun observeForLocationsList(): MutableLiveData<List<ListItemModel>>
    fun observeForNavigator(): MutableLiveData<String>
    fun observeForRetry(): MutableLiveData<Boolean>
    fun getSearchedLocation(newText: String?)
    fun getRecentlySearchedLocations()
    fun retry()
}