package com.example.haze.itmomaps.models

import com.example.haze.itmomaps.network.PictureView

data class Building(val name: String?, var floors: Array<PictureView>?, var numberOfFloors: Int)