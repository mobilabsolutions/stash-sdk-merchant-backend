package com.mobilabsolutions.server.commons.exception

import com.google.common.base.Preconditions.checkArgument
import org.springframework.http.HttpStatus

class ApiException : RuntimeException {

    private val status: HttpStatus
    private val apiError: ApiError

    companion object {
        const val STATUS_NON_ERROR_HTTP_STATUS_CODE = "status: non-error http status code"
    }

    constructor(apiError: ApiError) : super(apiError.message().orElse(null)) {
        this.status = apiError.httpStatus()
        this.apiError = apiError
        checkArgument(isErrorStatus(status), STATUS_NON_ERROR_HTTP_STATUS_CODE)
    }

    constructor(status: HttpStatus, apiError: ApiError) : super(apiError.message().orElse(null)) {
        this.status = status
        this.apiError = apiError
        checkArgument(isErrorStatus(status), STATUS_NON_ERROR_HTTP_STATUS_CODE)
    }

    constructor(
        message: String,
        status: HttpStatus,
        apiError: ApiError
    ) : super(message) {
        this.status = status
        this.apiError = apiError
        checkArgument(isErrorStatus(status), STATUS_NON_ERROR_HTTP_STATUS_CODE)
    }

    constructor(
        message: String,
        cause: Throwable,
        status: HttpStatus,
        apiError: ApiError
    ) : super(message, cause) {
        this.status = status
        this.apiError = apiError
        checkArgument(isErrorStatus(status), STATUS_NON_ERROR_HTTP_STATUS_CODE)
    }

    constructor(
        cause: Throwable,
        status: HttpStatus,
        apiError: ApiError
    ) : super(apiError.message().orElse(null), cause) {
        this.status = status
        this.apiError = apiError
        checkArgument(isErrorStatus(status), STATUS_NON_ERROR_HTTP_STATUS_CODE)
    }

    private fun isErrorStatus(status: HttpStatus): Boolean {
        return status.is4xxClientError || status.is5xxServerError
    }

    fun status() = status

    fun apiError() = apiError
}
