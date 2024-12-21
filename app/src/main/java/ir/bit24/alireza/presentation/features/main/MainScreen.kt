package ir.bit24.alireza.presentation.features.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ir.bit24.alireza.domain.base.Resource
import ir.bit24.alireza.domain.model.Station
import ir.bit24.alireza.presentation.ErrorMessage
import ir.bit24.alireza.presentation.LoadingDialog
import ir.bit24.alireza.presentation.navigation.Screen

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainVM = hiltViewModel()
) {
    val stationListRes by viewModel.stationListRes
    val selectedStationId by viewModel.selectedStationId
    val navigatedStationId by viewModel.navigatedStationId
    val lazyListState = rememberLazyListState()
    var searchQuery by remember { mutableStateOf("") }

    when (stationListRes) {
        is Resource.Error -> {
            ErrorMessage(
                modifier = Modifier.fillMaxSize(),
                message = (stationListRes as Resource.Error).errorModel.getErrorMessage(),
                onClickRetry = { viewModel.getList() })
        }

        is Resource.Loading -> LoadingDialog()
        is Resource.Success -> {
            val stations = (stationListRes as Resource.Success<List<Station>>).data
            val filteredStations =
                stations.filter { it.name.contains(searchQuery, ignoreCase = true) }
            Box(Modifier.fillMaxSize()) {
                StationsMapView(
                    modifier = Modifier.fillMaxSize(),
                    stations = filteredStations,
                    navigatedStationId = navigatedStationId,
                    onStationClick = { id ->
                        viewModel.selectStation(id)
                    })
                StationSearchBar(searchQuery = searchQuery, onSearchChange = { searchQuery = it })
                HorizontalStationList(
                    stations = filteredStations,
                    selectedStationId = selectedStationId,
                    onNavigateToStation = { id ->
                        viewModel.setNavigation(id)
                    },
                    onShowDetails = { station ->
                        if (selectedStationId != null)
                            navController.navigate("${Screen.DetailScreen.route}/${selectedStationId}")
                    },
                    modifier = Modifier.align(Alignment.BottomCenter),
                    lazyListState = lazyListState
                )
            }
            LaunchedEffect(selectedStationId) {
                if (selectedStationId != null)
                    stations.indexOfFirst { it.id == selectedStationId }.takeIf { it >= 0 }
                        ?.let { index ->
                            lazyListState.animateScrollToItem(index)
                        }
            }
        }

        else -> {}
    }

}

