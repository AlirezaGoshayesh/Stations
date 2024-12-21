package ir.bit24.alireza.data.local

import ir.bit24.alireza.data.local.model.StationEntity
import ir.bit24.alireza.data.local.room.StationDao
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val stationDao: StationDao) :
    LocalDataSource {
    override suspend fun insertStations(stations: List<StationEntity>) {
        stationDao.insertStations(stations)
    }

    override suspend fun getStations(): List<StationEntity> {
        return stationDao.getStations()
    }

    override suspend fun getStation(id: Long): StationEntity {
        return stationDao.getStations(id)
    }
}