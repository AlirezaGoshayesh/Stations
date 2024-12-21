package ir.bit24.alireza.presentation.features.detail

import android.R.attr.scaleX
import android.R.attr.scaleY
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.bit24.alireza.domain.base.Resource
import ir.bit24.alireza.domain.model.Station
import ir.bit24.alireza.presentation.ErrorMessage
import ir.bit24.alireza.presentation.LoadingDialog
import ir.bit24.alireza.presentation.features.main.HorizontalStationList
import ir.bit24.alireza.presentation.features.main.StationSearchBar
import ir.bit24.alireza.presentation.features.main.StationsMapView
import ir.bit24.alireza.presentation.navigation.Screen

@Composable
fun DetailScreen(
    id: Long,
    viewModel: DetailVM = hiltViewModel()
) {
    LaunchedEffect(id) {
        viewModel.getStation(id)
    }
    val stationRes by viewModel.stationRes
    when (stationRes) {
        is Resource.Error -> {
            ErrorMessage(
                modifier = Modifier.fillMaxSize(),
                message = (stationRes as Resource.Error).errorModel.getErrorMessage(),
                onClickRetry = { viewModel.getStation(id) })
        }

        is Resource.Loading -> LoadingDialog()
        is Resource.Success -> {
            val station = (stationRes as Resource.Success<Station>).data
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = 1.1f
                            scaleY = 1.1f
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = station.name,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )

                        HorizontalDivider(color = Color.Gray, thickness = 1.dp)

                        DetailRow(label = "Capacity", value = station.capacity.toString())

                        DetailRow(label = "Rental Method", value = station.rentalMethod)
                    }
                }
            }
        }

        else -> {}
    }
}


