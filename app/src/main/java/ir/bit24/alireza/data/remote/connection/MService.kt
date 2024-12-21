package ir.bit24.alireza.data.remote.connection

import ir.bit24.alireza.data.remote.model.StationDto
import retrofit2.http.GET

interface MService {

    /**
     * get stations
     */
    @GET("00313c22-a4c5-467f-b50c-ebb0fc58c58d")
    suspend fun getStations(): List<StationDto>

}