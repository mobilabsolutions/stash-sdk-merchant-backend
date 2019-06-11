package com.mobilabsolutions.payment.service

import com.mobilabsolutions.payment.common.util.RandomStringGenerator
import com.mobilabsolutions.payment.data.domain.PaymentMethod
import com.mobilabsolutions.payment.data.domain.User
import com.mobilabsolutions.payment.data.enum.PaymentMethodType
import com.mobilabsolutions.payment.data.repository.PaymentMethodRepository
import com.mobilabsolutions.payment.data.repository.UserRepository
import com.mobilabsolutions.payment.model.request.CreatePaymentMethodRequestModel
import com.mobilabsolutions.payment.paymentsdk.service.PaymentSdkService
import com.mobilabsolutions.server.commons.exception.ApiException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentMethodServiceTest {
    private val userId = "testuser12345678"
    private val wrongUserId = "wrong user id"
    private val paymentMethodId = "testpaymentmethod"
    private val wrongPaymentMethodId = "wrong payment method id"
    private val aliasId = "aliasId"
    private val ccExpiryMonth = "12"
    private val ccExpiryYear = "19"
    private val cardType = "VISA"
    private val cardMask = "1111"
    private val user = User(id = userId)

    @InjectMocks
    private lateinit var paymentMethodService: PaymentMethodService

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var paymentMethodRepository: PaymentMethodRepository

    @Mock
    private lateinit var paymentSdkService: PaymentSdkService

    @Mock
    private lateinit var randomStringGenerator: RandomStringGenerator

    @BeforeAll
    fun beforeAll() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(userRepository.getFirstById(userId)).thenReturn(user)
        Mockito.`when`(userRepository.getFirstById(wrongUserId)).thenReturn(null)
        Mockito.`when`(randomStringGenerator.generateRandomAlphanumeric(16)).thenReturn(paymentMethodId)
        Mockito.`when`(paymentMethodRepository.getFirstById(paymentMethodId)).thenReturn(
            PaymentMethod(paymentMethodId, true, aliasId, ccExpiryMonth, ccExpiryYear, cardType, cardMask, PaymentMethodType.CC, user)
        )
        Mockito.`when`(paymentMethodRepository.getFirstById(wrongPaymentMethodId)).thenReturn(null)
        Mockito.`when`(paymentMethodRepository.findAllByUserIdAndActive(userId, true)).thenReturn(
            listOf(PaymentMethod(paymentMethodId, true, aliasId, ccExpiryMonth, ccExpiryYear, cardType, cardMask, PaymentMethodType.CC, user))
        )
    }

    @Test
    fun `create payment method for existing user`() {
        paymentMethodService.createPaymentMethod(CreatePaymentMethodRequestModel(aliasId, ccExpiryMonth, ccExpiryYear, cardType, cardMask, PaymentMethodType.CC, userId))
    }

    @Test
    fun `create payment method for non existing user`() {
        Assertions.assertThrows(ApiException::class.java) {
            paymentMethodService.createPaymentMethod(CreatePaymentMethodRequestModel(aliasId, ccExpiryMonth, ccExpiryYear, cardType, cardMask, PaymentMethodType.CC, wrongUserId))
        }
    }

    @Test
    fun `delete payment method successfully`() {
        paymentMethodService.deletePaymentMethod(paymentMethodId)
    }

    @Test
    fun `delete payment method with wrong payment method id`() {
        Assertions.assertThrows(ApiException::class.java) {
            paymentMethodService.deletePaymentMethod(wrongPaymentMethodId)
        }
    }

    @Test
    fun `find all payment methods for user`() {
        paymentMethodService.findAllPaymentMethodsForUser(userId)
    }

    @Test
    fun `find all payment methods for non existing user`() {
        Assertions.assertThrows(ApiException::class.java) {
            paymentMethodService.findAllPaymentMethodsForUser(wrongUserId)
        }
    }
}
