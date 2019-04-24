package com.mobilabsolutions.server.commons.exception

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.common.base.MoreObjects
import com.google.common.base.Preconditions.checkNotNull
import com.google.common.collect.ImmutableMap
import org.springframework.http.HttpStatus
import java.util.Optional
import java.util.Objects

class ApiError private constructor(details: Map<String, Any>) {

    private val details: ImmutableMap<String, Any>

    init {
        this.details = ImmutableMap.copyOf(checkNotNull(details, DETAILS_PROPERTY))
    }

    companion object {

        private const val MESSAGE_PROPERTY = "message"
        private const val DETAILS_PROPERTY = "details"

        @JvmStatic
        fun ofMessage(message: String): ApiError {
            return ApiError(ImmutableMap.of<String, Any>(MESSAGE_PROPERTY, message))
        }

        fun ofDetails(details: Map<String, Any>): ApiError {
            return ApiError(details)
        }

        fun builder(): Builder {
            return Builder()
        }
    }

    fun asBadRequest(): ApiException {
        return ApiException(HttpStatus.BAD_REQUEST, this)
    }

    fun asUnauthorized(): ApiException {
        return ApiException(HttpStatus.UNAUTHORIZED, this)
    }

    fun asForbidden(): ApiException {
        return ApiException(HttpStatus.FORBIDDEN, this)
    }

    fun asNotFound(): ApiException {
        return ApiException(HttpStatus.NOT_FOUND, this)
    }

    fun asConflict(): ApiException {
        return ApiException(HttpStatus.CONFLICT, this)
    }

    fun asInternalServerError(): ApiException {
        return ApiException(HttpStatus.INTERNAL_SERVER_ERROR, this)
    }

    fun asInternalServerError(cause: Throwable): ApiException {
        return ApiException(cause, HttpStatus.INTERNAL_SERVER_ERROR, this)
    }

    fun asInternalServerError(exceptionMessage: String, cause: Throwable): ApiException {
        return ApiException(exceptionMessage, cause, HttpStatus.INTERNAL_SERVER_ERROR, this)
    }

    fun asInternalServerError(exceptionMessage: String): ApiException {
        return ApiException(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR, this)
    }

    fun asException(status: HttpStatus): ApiException {
        return ApiException(status, this)
    }

    fun asException(status: HttpStatus, exceptionMessage: String): ApiException {
        return ApiException(exceptionMessage, status, this)
    }

    fun asException(
        status: HttpStatus,
        exceptionMessage: String,
        cause: Throwable
    ): ApiException {
        return ApiException(exceptionMessage, cause, status, this)
    }

    fun asException(status: HttpStatus, cause: Throwable): ApiException {
        return ApiException(cause, status, this)
    }

    @JsonAnyGetter
    fun details(): Map<String, Any> {
        return details
    }

    @JsonIgnore
    fun message(): Optional<String> {
        val message = details[MESSAGE_PROPERTY]
        return if (message is String) Optional.of(message) else Optional.empty()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val apiError = other as ApiError?
        return details == apiError!!.details
    }

    override fun hashCode(): Int {
        return Objects.hash(details)
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
            .add(DETAILS_PROPERTY, details)
            .toString()
    }

    class Builder {

        private val builder = ImmutableMap.builder<String, Any>()

        fun withMessage(message: String): Builder {
            builder.put(MESSAGE_PROPERTY, message)
            return this
        }

        fun withProperty(name: String, value: Any): Builder {
            builder.put(name, value)
            return this
        }

        fun build(): ApiError {
            return ApiError(builder.build())
        }
    }
}
