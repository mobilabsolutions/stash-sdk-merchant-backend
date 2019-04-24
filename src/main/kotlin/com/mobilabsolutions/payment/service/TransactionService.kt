package com.mobilabsolutions.payment.service

import com.mobilabsolutions.payment.common.util.RandomStringGenerator
import com.mobilabsolutions.payment.data.domain.Transaction
import com.mobilabsolutions.payment.data.repository.PaymentMethodRepository
import com.mobilabsolutions.payment.data.repository.TransactionRepository
import com.mobilabsolutions.payment.model.request.PaymentRequestModel
import com.mobilabsolutions.payment.model.response.PaymentRespomseModel
import com.mobilabsolutions.payment.paymentsdk.PaymentSdkService
import com.mobilabsolutions.payment.paymentsdk.model.PaymentDataModel
import com.mobilabsolutions.payment.paymentsdk.model.request.AuthorizationRequestModel
import com.mobilabsolutions.server.commons.exception.ApiError
import mu.KLogging
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@Service
@Transactional
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val paymentSdkService: PaymentSdkService,
    private val randomStringGenerator: RandomStringGenerator
) {
    companion object : KLogging() {
        const val TRANSACTION_ID_LENGTH = 16
    }

    /**
     * Makes authorization for the given payment request.
     *
     * @param request payment request
     * @return payment response
     */
    fun authorize(request: PaymentRequestModel): PaymentRespomseModel {
        logger.info { "Executing transaction authorization" }
        val paymentMethod = paymentMethodRepository.getFirstById(request.paymentMethodId!!)
            ?: throw ApiError.ofMessage("Payment method cannot be found").asBadRequest()
        val transactionId = randomStringGenerator.generateRandomAlphanumeric(TRANSACTION_ID_LENGTH)

        val authorizationRequest = AuthorizationRequestModel(
            aliasId = paymentMethod.aliasId,
            paymentData = PaymentDataModel(
                amount = request.amount,
                currency = request.currency,
                reason = request.reason
            ),
            purchaseId = transactionId,
            customerId = paymentMethod.user.id
        )
        val authorizationResponse = paymentSdkService.authorization(authorizationRequest)

        val transaction = Transaction(
            id = transactionId,
            amount = authorizationResponse?.amount,
            currency = authorizationResponse?.currency,
            reason = request.reason,
            transactionId = authorizationResponse?.id,
            action = authorizationResponse?.action,
            status = authorizationResponse?.status,
            paymentMethod = paymentMethod
        )
        transactionRepository.save(transaction)
        return PaymentRespomseModel(
            transactionId, transaction.status,
            transaction.amount, transaction.currency,
            authorizationResponse?.additionalInfo)
    }
}
