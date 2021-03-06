package com.spdigital.seekweather.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.sample.gitrepos.extensions.filterNull
import com.spdigital.seekweather.R
import com.spdigital.seekweather.databinding.ActivityWeatherDetailBinding
import com.spdigital.seekweather.extensions.urImageCircular
import com.spdigital.seekweather.viewmodel.WeatherDetailViewModelImpl
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherDetailActivity : BaseActivity() {

    private lateinit var mBinding: ActivityWeatherDetailBinding
    private val weatherDetailViewModelImpl: WeatherDetailViewModelImpl by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_weather_detail)
        mBinding.viewmodel = weatherDetailViewModelImpl
        mBinding.lifecycleOwner = this

        weatherDetailViewModelImpl.getWeatherLiveData().observe(this, Observer {
            mBinding.weatherImage.urImageCircular(it?.currentCondition?.get(0)?.weatherIconUrl?.get(0)?.value.filterNull(), R.drawable.ic_launcher_background)
        })

        weatherDetailViewModelImpl.getWeatherDetails(intent?.getStringExtra("CITY").filterNull())
    }
}
