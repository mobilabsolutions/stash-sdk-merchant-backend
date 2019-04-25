package com.mobilabsolutions.payment.paymentsdk

import com.mobilabsolutions.payment.common.util.RandomStringGenerator
import com.mobilabsolutions.payment.paymentsdk.model.request.AuthorizationRequestModel
import com.mobilabsolutions.payment.paymentsdk.model.response.AuthorizationResponseModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@Service
class PaymentSdkService(
    private val restTemplate: RestTemplate,
    private val paymentSdkConfiguration: PaymentSdkConfiguration,
    private val randomStringGenerator: RandomStringGenerator
) {

    companion object {
        const val IDEMPOTENT_KEY_HEADER = "Idempotent-Key"
        const val SECRET_KEY_HEADER = "Secret-Key"
        const val PSP_TEST_MODE_HEADER = "PSP-Test-Mode"
        const val ALIAS_ID_PARAM = "Alias-Id"
        const val IDEMPOTENT_KEY_LENGTH = 23
    }

    fun authorization(authorizationRequestModel: AuthorizationRequestModel): AuthorizationResponseModel? {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_JSON
        httpHeaders.set(SECRET_KEY_HEADER, paymentSdkConfiguration.merchantSecretKey)
        httpHeaders.set(IDEMPOTENT_KEY_HEADER, randomStringGenerator.generateRandomAlphanumeric(IDEMPOTENT_KEY_LENGTH))
        httpHeaders.set(PSP_TEST_MODE_HEADER, paymentSdkConfiguration.testMode)
        return executeRestCall(
            paymentSdkConfiguration.authorizationUrl,
            HttpMethod.PUT,
            authorizationRequestModel,
            httpHeaders,
            AuthorizationResponseModel::class.java
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
        return restTemplate.exchange(url, httpMethod, httpEntity, responseClass, uriVariables).body
    }

    private fun <T, R> executeRestCall(
        url: String,
        httpMethod: HttpMethod,
        requestBody: T?,
        httpHeaders: HttpHeaders,
        responseClass: Class<R>
    ): R? {
        val httpEntity = HttpEntity(requestBody, httpHeaders)
        return restTemplate.exchange(url, httpMethod, httpEntity, responseClass).body
    }
}