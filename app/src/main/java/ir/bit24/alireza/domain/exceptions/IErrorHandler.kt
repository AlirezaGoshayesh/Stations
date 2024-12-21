package ir.bit24.alireza.domain.exceptions

import ir.bit24.alireza.domain.exceptions.ErrorModel

interface IErrorHandler {
    fun handleException(throwable: Throwable?): ErrorModel
}