package ir.bit24.alireza.data.local

import ir.bit24.alireza.data.local.model.StationEntity

interface LocalDataSource {
    suspend fun insertStations(stations: List<StationEntity>)
    suspend fun getStations(): List<StationEntity>
    suspend fun getStation(id: Long): StationEntity
}