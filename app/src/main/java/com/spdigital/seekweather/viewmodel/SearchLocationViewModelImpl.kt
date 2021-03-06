package com.spdigital.seekweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sample.gitrepos.extensions.filterNull
import com.spdigital.seekweather.network.Resource
import com.spdigital.seekweather.extensions.toSingleEvent
import com.spdigital.seekweather.model.search.LocationEntity
import com.spdigital.seekweather.model.search.LocationModel
import com.spdigital.seekweather.usecase.LocationUseCaseImpl
import com.spdigital.seekweather.view.ListItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchLocationViewModelImpl(private val locationUseCaseImpl: LocationUseCaseImpl) :
    BaseViewModel(), SearchLocationViewModel {
    private val locationsLiveData by lazy { MutableLiveData<List<ListItemModel>>().toSingleEvent() }
    private val navigateLiveData by lazy { MutableLiveData<String>().toSingleEvent() }
    private val retryLiveData by lazy { MutableLiveData<Boolean>().toSingleEvent() }

    override fun observeForLocationsList(): MutableLiveData<List<ListItemModel>> {
        return locationsLiveData
    }

    override fun observeForNavigator(): MutableLiveData<String> {
        return navigateLiveData
    }

    override fun observeForRetry(): MutableLiveData<Boolean> {
        return retryLiveData
    }

    override fun getSearchedLocation(query: String?) {
        setLoading()
        viewModelScope.launch(Dispatchers.Main) {
            delay(2000)
            val resource = locationUseCaseImpl.getLocation(query)
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    val locationModel = resource.data as LocationModel
                    if (locationModel.searchApi == null) {
                        setError()
                    } else {
                        val mapToListItem = locationUseCaseImpl.mapToItemViewList(locationModel.searchApi?.resultModel, getItemClickCallback()
                        )
                        locationsLiveData.value = mapToListItem
                        setSuccess()
                    }
                }
                Resource.Status.ERROR -> {
                    setError()
                }
            }
        }
    }

    override fun getRecentlySearchedLocations() {
        viewModelScope.launch {
            locationsLiveData.value = locationUseCaseImpl.mapRecentlySearchedLocationToListItem(locationUseCaseImpl.getSortedRecentlySearchedLocations(), getItemClickCallback())
            setSuccess()
        }
    }

    override fun retry() {
        retryLiveData.value = true
    }

    private fun getItemClickCallback(): (Any?) -> Unit {
        return {
            if (it != null && it is LocationEntity) {
                viewModelScope.launch {
                    locationUseCaseImpl.saveLocationToLocalCache(it)
                }
                navigateLiveData.value = it.areaName.filterNull()
            }
        }
    }

}