package com.mobilabsolutions.payment.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author <a href="mailto:mohamed.osman@mobilabsolutions.com">Mohamed Osman</a>
 */
@ApiModel("Merchant notifications model")
data class MerchantNotificationsModel(
    @ApiModelProperty(value = "Transaction id", example = "frtdqw7m")
    val transactionId: String?,

    @ApiModelProperty(value = "Transaction status", example = "Values: SUCCESS, FAIL")
    val transactionStatus: String?,

    @ApiModelProperty(value = "Transaction action", example = "Values: PREAUTH, AUTH, REVERSAL, REFUND, CAPTURE")
    val transactionAction: String?,

    @ApiModelProperty(value = "Payment method", example = "Values: CC, SEPA, PAY_PAL, GOOGLE_PAY, APPLE_PAY, KLARNA")
    val paymentMethod: String?,

    @ApiModelProperty(value = "Amount in smallest currency unit (e.g. cent)", example = "2000")
    val amount: Int?,

    @ApiModelProperty(value = "Currency", example = "EUR")
    val currency: String?,

    @ApiModelProperty(value = "Reason", example = "Payment for dinner")
    val reason: String?,

    @ApiModelProperty(value = "Notification creation date", example = "2018-12-31 23:59:59.999 +0100")
    val notificationCreatedDate: String?
)
