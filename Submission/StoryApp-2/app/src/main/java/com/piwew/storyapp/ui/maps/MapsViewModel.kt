package com.piwew.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import com.piwew.storyapp.data.repo.MapsRepository

class MapsViewModel(
    private val mapsRepository: MapsRepository
) : ViewModel() {

    fun getStoriesWithLocation() = mapsRepository.getStoriesWithLocation()
}