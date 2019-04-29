package com.mobilabsolutions.payment.paymentsdk.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.mobilabsolutions.payment.common.exception.MerchantError
import com.mobilabsolutions.payment.paymentsdk.model.request.PaymentSdkAuthorizationRequestModel
import com.mobilabsolutions.payment.paymentsdk.model.response.PaymentSdkAuthorizationResponseModel
import com.mobilabsolutions.payment.paymentsdk.model.response.PaymentSdkErrorResponseModel
import com.mobilabsolutions.server.commons.exception.ApiError
import com.mobilabsolutions.server.commons.exception.ApiException
import mu.KLogging
import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@Service
class PaymentSdkService(
    private val restTemplate: RestTemplate,
    private val paymentSdkConfiguration: PaymentSdkConfiguration,
    private val jsonMapper: ObjectMapper
) {

    companion object : KLogging() {
        const val IDEMPOTENT_KEY_HEADER = "Idempotent-Key"
        const val SECRET_KEY_HEADER = "Secret-Key"
        const val PSP_TEST_MODE_HEADER = "PSP-Test-Mode"
        const val ALIAS_ID_PARAM = "Alias-Id"
    }

    fun authorization(idempotentKey: String?, authorizationRequestModel: PaymentSdkAuthorizationRequestModel): PaymentSdkAuthorizationResponseModel? {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_JSON
        httpHeaders.set(SECRET_KEY_HEADER, paymentSdkConfiguration.merchantSecretKey)
        httpHeaders.set(IDEMPOTENT_KEY_HEADER, idempotentKey)
        httpHeaders.set(PSP_TEST_MODE_HEADER, paymentSdkConfiguration.testMode)
        return executeRestCall(
            paymentSdkConfiguration.authorizationUrl,
            HttpMethod.PUT,
            authorizationRequestModel,
            httpHeaders,
            PaymentSdkAuthorizationResponseModel::class.java
        )
    }

    fun deleteAlias(aliasId: String): Unit? {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_JSON
        httpHeaders.set(SECRET_KEY_HEADER, paymentSdkConfiguration.merchantSecretKey)
        httpHeaders.set(PSP_TEST_MODE_HEADER, paymentSdkConfiguration.testMode)
        val uriVariables = HashMap<String, String>()
        uriVariables[ALIAS_ID_PARAM] = aliasId
        return executeRestCallWithUriParams(
            paymentSdkConfiguration.deleteAliasUrl,
            HttpMethod.DELETE,
            null,
            httpHeaders,
            Unit::class.java,
            uriVariables
        )
    }

    private fun <T, R> executeRestCallWithUriParams(
        url: String,
        httpMethod: HttpMethod,
        requestBody: T?,
        httpHeaders: HttpHeaders,
        responseClass: Class<R>,
        uriVariables: MutableMap<String, String>
    ): R? {
        val httpEntity = HttpEntity(requestBody, httpHeaders)
        try {
            return restTemplate.exchange(url, httpMethod, httpEntity, responseClass, uriVariables).body
        } catch (exception: RestClientResponseException) {
            logger.error("Error during request to Payment SDK", exception)
            throw handleResponseException(exception)
        }
    }

    private fun <T, R> executeRestCall(
        url: String,
        httpMethod: HttpMethod,
        requestBody: T?,
        httpHeaders: HttpHeaders,
        responseClass: Class<R>
    ): R? {
        val httpEntity = HttpEntity(requestBody, httpHeaders)
        try {
            return restTemplate.exchange(url, httpMethod, httpEntity, responseClass).body
        } catch (exception: RestClientResponseException) {
            logger.error("Error during request to Payment SDK", exception)
            throw handleResponseException(exception)
        }
    }

    private fun handleResponseException(exception: RestClientResponseException): ApiException {
        val errorMessage = exception.responseBodyAsString
        if (StringUtils.isEmpty(errorMessage)) {
            throw ApiError.ofErrorCode(MerchantError.PAYMENT_SDK_ERROR, "Payment SDK error message is empty").asException()
        }
        val paymentSdkError = jsonMapper.readValue(errorMessage, PaymentSdkErrorResponseModel::class.java)
        logger.error("Error during request to PaymentSDK. Error code: {}, error message: {}",
            paymentSdkError.errorCode, paymentSdkError.errorDescription)
        return ApiError.builder()
            .withErrorCode(MerchantError.PAYMENT_SDK_ERROR)
            .withError(MerchantError.PAYMENT_SDK_ERROR.message)
            .withMessage(paymentSdkError.errorDescription!!)
            .build().asException(MerchantError.PAYMENT_SDK_ERROR)
    }
}
