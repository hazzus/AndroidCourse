package com.example.haze.itmomaps.api

// TODO no singleton
object MapsRepositoryProvider {
    fun provideMapRepository() : MapsRepository {
        return MapsRepository(MapsApiService.create())
    }
}