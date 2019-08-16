package com.mobilabsolutions.payment.data.enum

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
enum class TransactionAction {
    PREAUTH,
    AUTH,
    REVERSAL,
    REFUND,
    CAPTURE,
    CHARGEBACK,
    CHARGEBACK_REVERSED,
    ADDITIONAL;
}
