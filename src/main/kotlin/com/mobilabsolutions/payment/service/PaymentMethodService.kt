package com.mobilabsolutions.payment.service

import com.mobilabsolutions.payment.common.util.RandomStringGenerator
import com.mobilabsolutions.payment.data.domain.PaymentMethod
import com.mobilabsolutions.payment.data.repository.PaymentMethodRepository
import com.mobilabsolutions.payment.data.repository.UserRepository
import com.mobilabsolutions.payment.model.request.CreatePaymentMethodRequestModel
import com.mobilabsolutions.payment.model.response.CreatePaymentMethodResponseModel
import com.mobilabsolutions.payment.model.response.PaymentMethodListResponseModel
import com.mobilabsolutions.payment.model.response.PaymentMethodResponseModel
import com.mobilabsolutions.payment.paymentsdk.PaymentSdkService
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
        val user = userRepository.getFirstById(request.userId!!)
            ?: throw ApiError.ofMessage("User cannot be found").asBadRequest()
        logger.info("Creating payment method for user: {}", user)

        val paymentMethodId = randomStringGenerator.generateRandomAlphanumeric(PAYMENT_METHOD_ID_LENGTH)
        val paymentMethod = PaymentMethod(
            id = paymentMethodId,
            aliasId = request.aliasId,
            alias = request.alias,
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
            ?: throw ApiError.ofMessage("Payment method cannot be found").asBadRequest()
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
        val usersPaymentMethods = paymentMethodRepository.findAllByUserIdAndActive(userId, true)
        val paymentMethods = ArrayList<PaymentMethodResponseModel>()
        usersPaymentMethods.forEach { paymentMethods.add(PaymentMethodResponseModel(
            paymentMethodId = it.id,
            alias = it.alias,
            type = it.type
        )) }
        return PaymentMethodListResponseModel(paymentMethods)
    }
}
