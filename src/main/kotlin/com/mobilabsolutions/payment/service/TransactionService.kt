package com.mobilabsolutions.payment.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.mobilabsolutions.payment.common.exception.MerchantError
import com.mobilabsolutions.payment.common.util.RandomStringGenerator
import com.mobilabsolutions.payment.data.domain.PaymentMethod
import com.mobilabsolutions.payment.data.domain.Transaction
import com.mobilabsolutions.payment.data.enum.TransactionAction
import com.mobilabsolutions.payment.data.enum.TransactionStatus
import com.mobilabsolutions.payment.data.repository.PaymentMethodRepository
import com.mobilabsolutions.payment.data.repository.TransactionRepository
import com.mobilabsolutions.payment.model.request.MerchantNotificationsRequestModel
import com.mobilabsolutions.payment.model.request.PaymentRequestModel
import com.mobilabsolutions.payment.model.response.PaymentResponseModel
import com.mobilabsolutions.payment.paymentsdk.service.PaymentSdkService
import com.mobilabsolutions.payment.paymentsdk.model.PaymentDataModel
import com.mobilabsolutions.payment.paymentsdk.model.request.PaymentSdkAuthorizationRequestModel
import com.mobilabsolutions.payment.paymentsdk.model.response.PaymentSdkAuthorizationResponseModel
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
    private val randomStringGenerator: RandomStringGenerator,
    private val jsonMapper: ObjectMapper
) {
    companion object : KLogging() {
        const val TRANSACTION_ID_LENGTH = 16
        const val IDEMPOTENT_KEY_LENGTH = 23
    }

    /**
     * Makes authorization for the given payment request.
     *
     * @param request payment request
     * @return payment response
     */
    fun authorize(idempotentKey: String?, request: PaymentRequestModel): PaymentResponseModel {
        logger.info { "Executing transaction authorization" }
        val paymentMethod = paymentMethodRepository.getFirstById(request.paymentMethodId!!)
            ?: throw ApiError.ofErrorCode(MerchantError.PAYMENT_METHOD_NOT_FOUND).asException()
        val transactionId = randomStringGenerator.generateRandomAlphanumeric(TRANSACTION_ID_LENGTH)

        val authorizationRequest = PaymentSdkAuthorizationRequestModel(
            aliasId = paymentMethod.aliasId,
            paymentData = PaymentDataModel(
                amount = request.amount,
                currency = request.currency,
                reason = request.reason
            ),
            purchaseId = transactionId,
            customerId = paymentMethod.user.id
        )
        val idempotentKeyHeader = idempotentKey
            ?: randomStringGenerator.generateRandomAlphanumeric(IDEMPOTENT_KEY_LENGTH)
        val authorizationResponse = paymentSdkService.authorization(idempotentKeyHeader, authorizationRequest)

        return executeIdempotentTransaction(idempotentKeyHeader, TransactionAction.AUTH,
            paymentMethod, transactionId, request, authorizationResponse)
    }

    /**
     * Creates transaction record for notifications received
     *
     * @param merchantNotificationListRequestModel: Merchant notifications request model
     */
    fun createNotificationTransactionRecord(merchantNotificationListRequestModel: MerchantNotificationsRequestModel) {
        logger.info("Creating transaction record for received notifications")
        merchantNotificationListRequestModel.notifications.forEach {
            val transaction = transactionRepository.getTransactionsByTransactionIdAndAction(it.transactionId!!, it.transactionAction!!)
            val newTransaction = Transaction(
                transactionId = transaction!!.transactionId,
                currency = it.currency,
                amount = it.amount,
                reason = it.reason,
                status = TransactionStatus.valueOf(it.transactionStatus!!),
                action = TransactionAction.valueOf(it.transactionAction),
                paymentMethod = transaction.paymentMethod,
                paymentSdkResponse = transaction.paymentSdkResponse,
                notification = true,
                id = randomStringGenerator.generateRandomAlphanumeric(TRANSACTION_ID_LENGTH),
                idempotentKey = null
            )
            transactionRepository.save(newTransaction)
        }
    }

    private fun executeIdempotentTransaction(
        idempotentKey: String,
        action: TransactionAction,
        paymentMethod: PaymentMethod,
        transactionId: String,
        request: PaymentRequestModel,
        authorizationResponse: PaymentSdkAuthorizationResponseModel?
    ): PaymentResponseModel {
        val transaction = transactionRepository.getByIdempotentKeyAndActionAndPaymentMethod(idempotentKey, action, paymentMethod)

        when {
            transaction != null -> return PaymentResponseModel(
                transaction.id,
                transaction.status,
                transaction.amount,
                transaction.currency,
                jsonMapper.readValue(transaction.paymentSdkResponse, PaymentSdkAuthorizationResponseModel::class.java)?.additionalInfo
            )
            else -> {

                val newTransaction = Transaction(
                    id = transactionId,
                    amount = authorizationResponse?.amount,
                    currency = authorizationResponse?.currency,
                    reason = request.reason,
                    transactionId = authorizationResponse?.id,
                    idempotentKey = idempotentKey,
                    action = authorizationResponse?.action,
                    status = authorizationResponse?.status,
                    paymentSdkResponse = jsonMapper.writeValueAsString(authorizationResponse),
                    paymentMethod = paymentMethod
                )
                transactionRepository.save(newTransaction)
                return PaymentResponseModel(
                    transactionId, newTransaction.status,
                    newTransaction.amount, newTransaction.currency,
                    authorizationResponse?.additionalInfo)
            }
        }
    }
}
