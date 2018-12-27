package com.example.haze.itmomaps.api

object MapsRepositoryProvider {
    fun provideMapRepository() : MapsRepository {
        return MapsRepository(MapsApiService.create())
    }
}