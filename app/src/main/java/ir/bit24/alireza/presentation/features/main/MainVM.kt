package ir.bit24.alireza.presentation.features.main

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.bit24.alireza.domain.GetStationsUseCase
import ir.bit24.alireza.domain.base.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.bit24.alireza.domain.model.Station
import ir.bit24.alireza.presentation.util.NetworkUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject
constructor(
    @ApplicationContext private val context: Context,
    private val getStationsUseCase: GetStationsUseCase,
) : ViewModel() {

    private val _stationListRes = mutableStateOf<Resource<List<Station>>?>(Resource.Loading)
    val stationListRes: State<Resource<List<Station>>?> get() = _stationListRes
    private val _selectedStationId = mutableStateOf<Long?>(null)
    val selectedStationId: State<Long?> = _selectedStationId
    private val _navigatedStationId = mutableStateOf<Long?>(null)
    val navigatedStationId: State<Long?> = _navigatedStationId

    init {
        getList()
    }

    fun getList() {
        viewModelScope.launch {
            val offline = !NetworkUtils.isInternetAvailable(context)
            _stationListRes.value = getStationsUseCase(offline)
        }
    }

    fun selectStation(stationId: Long?) {
        _selectedStationId.value = stationId
    }

    fun setNavigation(stationId: Long?) {
        _navigatedStationId.value = stationId
        _selectedStationId.value = stationId
    }
}
