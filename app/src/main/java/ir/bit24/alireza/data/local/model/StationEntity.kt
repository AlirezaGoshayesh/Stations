package ir.bit24.alireza.data.local.model

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "stations")
data class StationEntity(
        @PrimaryKey val id: Long,
        val name: String,
        val capacity: Int,
        val lat: Double,
        val lon: Double,
        val rentalMethod: String
)