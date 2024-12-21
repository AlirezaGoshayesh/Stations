package ir.bit24.alireza.data.remote

import ir.bit24.alireza.data.remote.model.StationDto

interface RemoteDataSource {
    suspend fun getStations(): List<StationDto>
}