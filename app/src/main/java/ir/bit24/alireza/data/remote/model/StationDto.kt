package ir.bit24.alireza.data.remote.model

data class StationDto(
    val capacity: String,
    val lat: String,
    val lon: String,
    val name: String,
    val rental_method: String,
    val station_id: String
)