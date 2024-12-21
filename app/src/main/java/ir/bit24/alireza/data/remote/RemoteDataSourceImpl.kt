package ir.bit24.alireza.data.remote

import ir.bit24.alireza.data.remote.connection.MService
import ir.bit24.alireza.data.remote.model.StationDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val service: MService) : RemoteDataSource {
    override suspend fun getStations(): List<StationDto> {
        return service.getStations()
    }
}