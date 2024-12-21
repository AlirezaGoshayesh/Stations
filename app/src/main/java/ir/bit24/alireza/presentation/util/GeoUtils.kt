package ir.bit24.alireza.presentation.util

import org.osmdroid.util.GeoPoint

object GeoUtils {

    fun calculateCenterPoint(stationPoints: List<GeoPoint>): GeoPoint {
        if (stationPoints.isEmpty()) return GeoPoint(37.814022, 144.939521)
        val avgLat = stationPoints.map { it.latitude }.average()
        val avgLon = stationPoints.map { it.longitude }.average()
        return GeoPoint(avgLat, avgLon)
    }

}