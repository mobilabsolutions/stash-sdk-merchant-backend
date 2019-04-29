package com.mobilabsolutions.server.commons.exception

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.common.base.MoreObjects
import com.google.common.base.Preconditions.checkNotNull
import com.google.common.collect.ImmutableMap
import com.mobilabsolutions.payment.common.exception.MerchantError
import org.springframework.http.HttpStatus
import java.util.Optional
import java.util.Objects

class ApiError private constructor(details: Map<String, Any>) {

    private val details: ImmutableMap<String, Any>
    private lateinit var httpStatus: HttpStatus

    init {
        this.details = ImmutableMap.copyOf(checkNotNull(details, DETAILS_PROPERTY))
    }

    private constructor(details: Map<String, Any>, httpStatus: HttpStatus) : this(details) {
        this.httpStatus = httpStatus
    }

    companion object {

        private const val MESSAGE_PROPERTY = "error_description"
        private const val CODE_PROPERTY = "error_code"
        private const val ERROR_PROPERTY = "error"
        private const val DETAILS_PROPERTY = "details"

        fun ofMessage(message: String): ApiError {
            return ApiError(ImmutableMap.of<String, Any>(MESSAGE_PROPERTY, message))
        }

        fun ofError(error: String): ApiError {
            return ApiError(ImmutableMap.of<String, Any>(ERROR_PROPERTY, error))
        }

        fun ofErrorCode(errorCode: MerchantError, message: String? = null): ApiError {
            return ApiError(ImmutableMap.of<String, Any>(CODE_PROPERTY, errorCode.code, MESSAGE_PROPERTY, message ?: errorCode.message), errorCode.httpStatus)
        }

        fun ofDetails(details: Map<String, Any>): ApiError {
            return ApiError(details)
        }

        fun builder(): Builder {
            return Builder()
        }
    }

    fun asException(): ApiException {
        return ApiException(this)
    }

    fun asException(errorCode: MerchantError): ApiException {
        return ApiException(errorCode.httpStatus, this)
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

    @JsonIgnore
    fun httpStatus(): HttpStatus {
        return httpStatus
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

        fun withErrorCode(errorCode: MerchantError): Builder {
            builder.put(CODE_PROPERTY, errorCode.code)
            return this
        }

        fun withError(error: String): Builder {
            builder.put(ERROR_PROPERTY, error)
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
