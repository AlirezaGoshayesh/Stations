package ir.bit24.alireza.data

import ir.bit24.alireza.data.local.LocalDataSource
import ir.bit24.alireza.data.local.model.StationEntity
import ir.bit24.alireza.data.remote.RemoteDataSource
import ir.bit24.alireza.domain.Repository
import ir.bit24.alireza.domain.model.Station
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {
    override suspend fun getStations(offline: Boolean): List<Station> {
        //This mappings can be done in a mapper too.
        return if (offline) {
            localDataSource.getStations().map {
                Station(
                    capacity = it.capacity,
                    id = it.id,
                    lat = it.lat,
                    lon = it.lon,
                    name = it.name,
                    rentalMethod = it.rentalMethod
                )
            }
        } else {
            val stations = remoteDataSource.getStations().map {
                StationEntity(
                    id = it.station_id.toLong(),
                    name = it.name,
                    capacity = it.capacity.toInt(),
                    lat = it.lat.toDouble(),
                    lon = it.lon.toDouble(),
                    rentalMethod = it.rental_method
                )
            }
            localDataSource.insertStations(stations)
            stations.map {
                Station(
                    capacity = it.capacity,
                    id = it.id,
                    lat = it.lat,
                    lon = it.lon,
                    name = it.name,
                    rentalMethod = it.rentalMethod
                )
            }
        }
    }

    override suspend fun getStation(id: Long): Station {
        val stationEntity = localDataSource.getStation(id)
        return Station(
            capacity = stationEntity.capacity,
            id = stationEntity.id,
            lat = stationEntity.lat,
            lon = stationEntity.lon,
            name = stationEntity.name,
            rentalMethod = stationEntity.rentalMethod
        )
    }

}