package ir.bit24.alireza.domain

import ir.bit24.alireza.domain.model.Station

interface Repository {
    suspend fun getStations(offline: Boolean): List<Station>
    suspend fun getStation(id: Long): Station
}
