package ir.bit24.alireza.domain.model

data class Station(
    val capacity: Int,
    val lat: Double,
    val lon: Double,
    val name: String,
    val rentalMethod: String,
    val id: Long
)