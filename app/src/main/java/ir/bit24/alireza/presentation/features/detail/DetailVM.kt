package ir.bit24.alireza.presentation.features.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.bit24.alireza.domain.GetStationUseCase
import ir.bit24.alireza.domain.base.Resource
import ir.bit24.alireza.domain.model.Station
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailVM @Inject
constructor(
    private val getStationUseCase: GetStationUseCase
) : ViewModel() {

    private val _stationRes = mutableStateOf<Resource<Station>?>(Resource.Loading)
    val stationRes: State<Resource<Station>?> get() = _stationRes

    fun getStation(id: Long) {
        viewModelScope.launch {
            _stationRes.value = getStationUseCase(id)
        }
    }
}
