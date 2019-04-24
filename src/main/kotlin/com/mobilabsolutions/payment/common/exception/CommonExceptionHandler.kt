package com.mobilabsolutions.server.commons.exception

import mu.KLogging
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.validation.FieldError
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.MultipartException
import org.springframework.web.servlet.NoHandlerFoundException
import java.util.Arrays
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException

@ResponseBody
@ControllerAdvice
class CommonExceptionHandler {

    companion object : KLogging()

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(AccessDeniedException::class)
    fun accessDeniedException(): ApiError {
        return ApiError.ofMessage("Authenticated user doesn't have the required rights for this operation")
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(exception: NoHandlerFoundException): ApiError {
        logger.error(
            "No mapping found for request: " + exception.httpMethod +
                " " + exception.requestURL
        )
        return ApiError.ofMessage("nothing.here")
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(exception: ConstraintViolationException): ApiError {
        logger.error("Bad request", exception)
        return ApiError.builder()
            .withMessage("argument.validation.error")
            .withProperty("errors", exception.message ?: "non readable message")
            .build()
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleMissingRequestHeaderException(exception: MissingRequestHeaderException): ApiError {
        logger.error("Bad request", exception)
        return ApiError.builder()
            .withMessage("argument.validation.error")
            .withProperty("errors", exception.message)
            .build()
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ApiError {
        logger.error("Bad request", exception)
        val bindingResult = exception.bindingResult
        val errorMessage = bindingResult
            .allErrors
            .stream()
            .map { (it as FieldError).field + ": " + it.defaultMessage }
            .collect(Collectors.joining(", "))
        return ApiError.builder()
            .withMessage("argument.validation.error")
            .withProperty("errors", errorMessage)
            .build()
    }

    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(
        exception: HttpRequestMethodNotSupportedException
    ): ApiError {
        val message = ("Used method: [" +
            exception.method + "] " + "Supported methods: " +
            Arrays.toString(exception.supportedMethods))
        logger.error("Method not allowed", message)
        return ApiError.ofMessage("method.not.allowed")
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException): ApiError {
        logger.error("Bad request", exception)
        return ApiError.builder()
            .withMessage("argument.validation.error")
            .withProperty("errors", "non readable message")
            .build()
    }

    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleHttpMediaTypeNotSupportedException(
        exception: HttpMediaTypeNotSupportedException
    ): ApiError {
        val message = "Unsupported media type: " + exception.contentType
        logger.error(message, exception)
        return ApiError.ofMessage("unsupported.media.type")
    }

    /**
     * Handles the exception that is thrown when there is a missing request query parameter. <br></br>
     * See [here](http://stackoverflow.com/a/10323055/1005102).
     *
     * @param exception the thrown exception
     * @return the error response DTO
     */
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(UnsatisfiedServletRequestParameterException::class)
    fun handleUnsatisfiedServletRequestParameterException(
        exception: UnsatisfiedServletRequestParameterException
    ): ApiError {
        logger.error("Unprocessable entity", exception)
        return ApiError.ofMessage("missing.request.parameter")
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(
        exception: MissingServletRequestParameterException
    ): ApiError {
        logger.error("Unprocessable entity", exception)
        return ApiError.ofMessage("missing.request.parameter")
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MultipartException::class)
    fun handleMultipartException(exception: MultipartException): ApiError {
        if (exception is MaxUploadSizeExceededException) {
            logger.error("Multipart request is bigger than max allowed limit", exception)
            val maxUploadSize = exception.maxUploadSize
            return ApiError.builder()
                .withMessage("multipart.too.big")
                .withProperty("max.allowed.size.in.bytes", maxUploadSize)
                .build()
        }

        logger.error("Multipart request expected, but not a multipart", exception)
        return ApiError.ofMessage("request.not.multipart")
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchExceptionException(
        exception: MethodArgumentTypeMismatchException
    ): ApiError {
        logger.error("Method argument type mismatch", exception)
        return ApiError.builder()
            .withMessage("invalid.argument.type")
            .withProperty("requiredType", exception.requiredType?.simpleName ?: "")
            .withProperty("argumentName", exception.name)
            .withProperty("value", exception.value ?: "")
            .build()
    }

    @ExceptionHandler(ApiException::class)
    fun handleApiException(apiException: ApiException): ResponseEntity<ApiError> {
        val status = apiException.status()
        when {
            status.is4xxClientError -> logger.error("Api error", apiException)
            status.is5xxServerError -> logger.error("Api error", apiException)
            else -> logger.warn(
                "Api exception with non-error " + "http status code. Should never happen!", apiException
            )
        }
        return ResponseEntity(apiException.apiError(), status)
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleException(request: HttpServletRequest, exception: Exception): ApiError {
        logger.error(
            "Error on request: [{} {}]. Message: {}",
            request.method,
            request.requestURI,
            exception.message,
            exception
        )
        return ApiError.ofMessage("internal.error")
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(ResourceAccessException::class)
    fun handleResourceAccessException(exception: ResourceAccessException): ApiError {
        logger.error("Service unavailable.", exception)
        return ApiError.builder()
            .withMessage("service.unavailable")
            .withProperty("errors", exception.message!!)
            .build()
    }
}
