package com.mobilabsolutions.payment.common.exception

import org.springframework.http.HttpStatus

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
enum class MerchantError(val code: String, val message: String, val httpStatus: HttpStatus) {
    USER_NOT_FOUND("1000", "User cannot be found", HttpStatus.BAD_REQUEST),
    PAYMENT_METHOD_NOT_FOUND("1001", "Payment method cannot be found", HttpStatus.BAD_REQUEST),

    PAYMENT_SDK_ERROR("2000", "Unexpected Payment SDK error", HttpStatus.INTERNAL_SERVER_ERROR)
}
