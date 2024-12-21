package ir.bit24.alireza.data.local.room

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query

import ir.bit24.alireza.data.local.model.StationEntity;

@Dao
interface StationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<StationEntity>)

    @Query("SELECT * FROM stations")
    suspend fun getStations(): List<StationEntity>

    @Query("SELECT * FROM stations WHERE id = :id")
    suspend fun getStations(id: Long): StationEntity
}