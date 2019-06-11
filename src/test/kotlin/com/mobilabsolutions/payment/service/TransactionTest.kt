package com.mobilabsolutions.payment.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.mobilabsolutions.payment.common.configuration.CommonConfiguration
import com.mobilabsolutions.payment.common.util.RandomStringGenerator
import com.mobilabsolutions.payment.data.domain.PaymentMethod
import com.mobilabsolutions.payment.data.domain.User
import com.mobilabsolutions.payment.data.enum.PaymentMethodType
import com.mobilabsolutions.payment.data.enum.TransactionAction
import com.mobilabsolutions.payment.data.enum.TransactionStatus
import com.mobilabsolutions.payment.data.repository.PaymentMethodRepository
import com.mobilabsolutions.payment.data.repository.TransactionRepository
import com.mobilabsolutions.payment.model.request.PaymentRequestModel
import com.mobilabsolutions.payment.paymentsdk.service.PaymentSdkService
import com.mobilabsolutions.payment.paymentsdk.model.PaymentDataModel
import com.mobilabsolutions.payment.paymentsdk.model.request.PaymentSdkAuthorizationRequestModel
import com.mobilabsolutions.payment.paymentsdk.model.response.PaymentSdkAuthorizationResponseModel
import com.mobilabsolutions.server.commons.exception.ApiException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.MockitoAnnotations
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionTest {
    private val userId = "testuser12345678"
    private val transactionId = "testtransactionid"
    private val paymentSdkTransactionId = "1234"
    private val paymentMethodId = "testpaymentmethod"
    private val wrongPaymentMethodId = "wrong payment method id"
    private val aliasId = "aliasId"
    private val ccExpiryMonth = "12"
    private val ccExpiryYear = "19"
    private val cardType = "VISA"
    private val cardMask = "1111"
    private val user = User(id = userId)
    private val amount = 300
    private val currency = "EUR"
    private val reason = "Book"
    private val idempotentKey = "idempotentKey123"

    @InjectMocks
    private lateinit var transactionService: TransactionService

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    @Mock
    private lateinit var paymentMethodRepository: PaymentMethodRepository

    @Mock
    private lateinit var paymentSdkService: PaymentSdkService

    @Mock
    private lateinit var randomStringGenerator: RandomStringGenerator

    @Spy
    val objectMapper: ObjectMapper = CommonConfiguration().jsonMapper()

    @BeforeAll
    fun beforeAll() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(paymentMethodRepository.getFirstById(paymentMethodId)).thenReturn(
            PaymentMethod(paymentMethodId, true, aliasId, ccExpiryMonth, ccExpiryYear, cardType, cardMask, PaymentMethodType.CC, user)
        )
        Mockito.`when`(paymentMethodRepository.getFirstById(wrongPaymentMethodId)).thenReturn(null)

        Mockito.`when`(paymentSdkService.authorization(
            idempotentKey, PaymentSdkAuthorizationRequestModel(aliasId, PaymentDataModel(amount, currency, reason), transactionId, userId)
        )
        ).thenReturn(
            PaymentSdkAuthorizationResponseModel(paymentSdkTransactionId, amount, currency, TransactionStatus.SUCCESS, TransactionAction.AUTH, null)
        )
    }

    @Test
    fun `successful authorization`() {
        transactionService.authorize(idempotentKey, PaymentRequestModel(amount, currency, reason, paymentMethodId))
    }

    @Test
    fun `authorization with wrong payment method`() {
        Assertions.assertThrows(ApiException::class.java) {
            transactionService.authorize(idempotentKey, PaymentRequestModel(amount, currency, reason, wrongPaymentMethodId))
        }
    }
}
