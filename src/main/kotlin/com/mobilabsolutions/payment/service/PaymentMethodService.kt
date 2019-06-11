package com.mobilabsolutions.payment.service

import com.mobilabsolutions.payment.common.exception.MerchantError
import com.mobilabsolutions.payment.common.util.RandomStringGenerator
import com.mobilabsolutions.payment.data.domain.PaymentMethod
import com.mobilabsolutions.payment.data.repository.PaymentMethodRepository
import com.mobilabsolutions.payment.data.repository.UserRepository
import com.mobilabsolutions.payment.model.request.CreatePaymentMethodRequestModel
import com.mobilabsolutions.payment.model.response.CreatePaymentMethodResponseModel
import com.mobilabsolutions.payment.model.response.PaymentMethodListResponseModel
import com.mobilabsolutions.payment.model.response.PaymentMethodResponseModel
import com.mobilabsolutions.payment.paymentsdk.service.PaymentSdkService
import com.mobilabsolutions.server.commons.exception.ApiError
import mu.KLogging
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@Service
@Transactional
class PaymentMethodService(
    private val paymentMethodRepository: PaymentMethodRepository,
    private val userRepository: UserRepository,
    private val paymentSdkService: PaymentSdkService,
    private val randomStringGenerator: RandomStringGenerator
) {
    companion object : KLogging() {
        const val PAYMENT_METHOD_ID_LENGTH = 16
    }

    /**
     * Creates payment method for the given request.
     *
     * @param request create payment method request
     * @return create payment method response
     */
    fun createPaymentMethod(request: CreatePaymentMethodRequestModel): CreatePaymentMethodResponseModel {
        logger.info("Creating payment method for user {}", request.userId)
        val user = userRepository.getFirstById(request.userId!!)
            ?: throw ApiError.ofErrorCode(MerchantError.USER_NOT_FOUND).asException()

        val paymentMethodId = randomStringGenerator.generateRandomAlphanumeric(PAYMENT_METHOD_ID_LENGTH)
        val paymentMethod = PaymentMethod(
            id = paymentMethodId,
            aliasId = request.aliasId,
            ccExpiryMonth = request.ccExpiryMonth,
            ccExpiryYear = request.ccExpiryYear,
            cardType = request.cardType,
            cardMask = request.cardMask,
            type = request.type,
            user = user
        )
        paymentMethodRepository.save(paymentMethod)
        return CreatePaymentMethodResponseModel(paymentMethodId)
    }

    /**
     * Deactivates the payment method for the given payment method id.
     *
     * @param paymentMethodId payment method id
     */
    fun deletePaymentMethod(paymentMethodId: String) {
        logger.info("Deleting payment method {}", paymentMethodId)
        val paymentMethod = paymentMethodRepository.getFirstById(paymentMethodId)
            ?: throw ApiError.ofErrorCode(MerchantError.PAYMENT_METHOD_NOT_FOUND).asException()

        paymentSdkService.deleteAlias(paymentMethod.aliasId!!)
        paymentMethod.active = false
        paymentMethodRepository.save(paymentMethod)
    }

    /**
     * Returns all payment methods for the given user id.
     *
     * @param userId user id
     * @return list of payment methods
     */
    fun findAllPaymentMethodsForUser(userId: String): PaymentMethodListResponseModel {
        logger.info("Getting all payment methods for user {}", userId)
        userRepository.getFirstById(userId)
            ?: throw ApiError.ofErrorCode(MerchantError.USER_NOT_FOUND).asException()

        val usersPaymentMethods = paymentMethodRepository.findAllByUserIdAndActive(userId, true)
        val paymentMethods = ArrayList<PaymentMethodResponseModel>()
        usersPaymentMethods.forEach { paymentMethods.add(PaymentMethodResponseModel(
            paymentMethodId = it.id,
            ccExpiryMonth = it.ccExpiryMonth,
            ccExpiryYear = it.ccExpiryYear,
            cardType = it.cardType,
            cardMask = it.cardMask,
            type = it.type
        )) }
        return PaymentMethodListResponseModel(paymentMethods)
    }
}
