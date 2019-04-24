package com.mobilabsolutions.payment.model.response

import com.mobilabsolutions.payment.data.enum.TransactionStatus
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@ApiModel(value = "Payment Response")
data class PaymentRespomseModel(
    @ApiModelProperty(value = "Transaction ID", example = "jsjcidhvsdhhoie")
    val transactionId: String?,

    @ApiModelProperty(value = "Transaction status", example = "SUCCESS")
    val status: TransactionStatus?,

    @ApiModelProperty(value = "Amount in smallest currency unit (e.g. cent)", example = "300")
    val amount: Int?,

    @ApiModelProperty(value = "Currency", example = "EUR")
    val currency: String?,

    @ApiModelProperty(value = "Additional PSP info", example = "error")
    val additionalInfo: String?
)
