package ir.bit24.alireza.domain;

import ir.bit24.alireza.domain.base.UseCase
import ir.bit24.alireza.domain.exceptions.IErrorHandler
import ir.bit24.alireza.domain.model.Station
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStationUseCase @Inject constructor(
    private val repository: Repository,
    errorHandler: IErrorHandler
) : UseCase<Long, Station>(errorHandler) {
    override suspend fun execute(parameters: Long): Station {
        return repository.getStation(parameters)
    }
}