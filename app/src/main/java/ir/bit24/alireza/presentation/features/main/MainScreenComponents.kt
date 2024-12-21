package ir.bit24.alireza.presentation.features.main


import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import ir.bit24.alireza.domain.model.Station
import ir.bit24.alireza.presentation.util.GeoUtils.calculateCenterPoint
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay

@Composable
fun StationsMapView(
    modifier: Modifier = Modifier,
    stations: List<Station>,
    navigatedStationId: Long?,
    onStationClick: (Long) -> Unit
) {
    val centerPoint by remember(stations) {
        derivedStateOf {
            calculateCenterPoint(stations.map {
                GeoPoint(it.lat, it.lon)
            })
        }
    }

    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            controller.setZoom(14.0)
            controller.setCenter(centerPoint)
            isHorizontalMapRepetitionEnabled = false
            isVerticalMapRepetitionEnabled = false
            setMultiTouchControls(true)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier.fillMaxSize()
    ) { mapView ->
        mapView.overlays.clear()

        stations.forEach { station ->
            val geoPoint = GeoPoint(station.lat, station.lon)
            val capacityFactor = station.capacity / 10f
            val circleRadius = (20f * capacityFactor).coerceAtLeast(10f)
            val circleOverlay = object : Overlay() {
                override fun draw(
                    canvas: android.graphics.Canvas?,
                    mapView: MapView?,
                    shadow: Boolean
                ) {
                    if (canvas == null || mapView == null) return

                    val projection = mapView.projection
                    val point = projection.toPixels(geoPoint, null)

                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.argb(128, 0, 0, 255)
                        style = android.graphics.Paint.Style.FILL
                    }

                    canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), circleRadius, paint)

                    paint.apply {
                        color = android.graphics.Color.BLACK
                        style = android.graphics.Paint.Style.STROKE
                        strokeWidth = 2f
                    }
                    canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), circleRadius, paint)
                }

                override fun onSingleTapConfirmed(e: MotionEvent?, mapView: MapView?): Boolean {
                    val clickedGeoPoint =
                        mapView?.projection?.fromPixels(e?.x?.toInt() ?: 0, e?.y?.toInt() ?: 0)
                    if (clickedGeoPoint != null) {
                        val projection = mapView.projection
                        val clickedPoint = projection.toPixels(clickedGeoPoint, null)
                        val geoPointScreen = projection.toPixels(geoPoint, null)

                        val dx = clickedPoint.x - geoPointScreen.x
                        val dy = clickedPoint.y - geoPointScreen.y
                        val distanceInPixels = kotlin.math.sqrt((dx * dx + dy * dy).toDouble())

                        if (distanceInPixels < circleRadius * 1.1) {
                            onStationClick(station.id)
                            return true
                        }
                    }
                    return false
                }
            }

            mapView.overlays.add(circleOverlay)
        }

        navigatedStationId?.let { stationId ->
            stations.find { it.id == stationId }?.let { station ->
                val geoPoint = GeoPoint(station.lat, station.lon)
                mapView.controller.animateTo(geoPoint)
            }
        }
        mapView.invalidate()

    }
}

@Composable
fun StationSearchBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit
) {
    TextField(
        value = searchQuery,
        onValueChange = { onSearchChange(it) },
        label = {
            Text(
                text = "Search Stations",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(MaterialTheme.shapes.medium),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(0.8f)
        ),
        singleLine = true,
    )
}

@Composable
fun HorizontalStationList(
    stations: List<Station>,
    selectedStationId: Long?,
    onNavigateToStation: (Long) -> Unit,
    onShowDetails: (Long) -> Unit,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        state = lazyListState,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(stations.size) { index ->
            StationItem(
                station = stations[index],
                isSelected = stations[index].id == selectedStationId,
                onNavigateToStation = { onNavigateToStation(stations[index].id) },
                onShowDetails = { onShowDetails(stations[index].id) }
            )
        }
    }
}

@Composable
fun StationItem(
    station: Station,
    isSelected: Boolean,
    onNavigateToStation: () -> Unit,
    onShowDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(width = 220.dp, height = 140.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color(0xFF3F51B5) else Color.LightGray,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = station.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E88E5)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionButton(
                    text = "Navigate",
                    onClick = onNavigateToStation,
                    color = Color(0xFF1E88E5)
                )
                ActionButton(
                    text = "Details",
                    onClick = onShowDetails,
                    color = Color(0xFF43A047)
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    color: Color
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(36.dp)
    ) {
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}