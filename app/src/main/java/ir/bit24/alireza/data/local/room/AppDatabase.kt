package ir.bit24.alireza.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.bit24.alireza.data.local.model.StationEntity

@Database(
    entities = [StationEntity::class], version = 1, exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun stationDao(): StationDao
}