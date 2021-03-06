package com.mobilabsolutions.payment.data.repository

import com.mobilabsolutions.payment.data.domain.PaymentMethod
import com.mobilabsolutions.payment.data.domain.Transaction
import com.mobilabsolutions.payment.data.enum.TransactionAction
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
interface TransactionRepository : BaseRepository<Transaction, String> {
    @Query("SELECT DISTINCT tr FROM Transaction tr WHERE tr.idempotentKey = :idempotentKey AND tr.action = :action AND tr.paymentMethod = :paymentMethod")
    fun getByIdempotentKeyAndActionAndPaymentMethod(@Param("idempotentKey") idempotentKey: String, @Param("action") action: TransactionAction, @Param("paymentMethod") paymentMethod: PaymentMethod): Transaction?

    @Query("SELECT * FROM transaction_record tr WHERE CAST(tr.payment_sdk_response AS json)#>>'{id}' = :transactionId AND tr.action = :action", nativeQuery = true)
    fun getTransactionBySdkTransactionIdAndAction(@Param("transactionId") transactionId: String, @Param("action") action: String): Transaction?
}
