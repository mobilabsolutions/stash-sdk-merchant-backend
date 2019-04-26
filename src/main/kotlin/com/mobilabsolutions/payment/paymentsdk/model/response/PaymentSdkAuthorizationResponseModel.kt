package com.mobilabsolutions.payment.paymentsdk.model.response

import com.mobilabsolutions.payment.data.enum.TransactionAction
import com.mobilabsolutions.payment.data.enum.TransactionStatus
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@ApiModel(value = "Authorization Response")
data class PaymentSdkAuthorizationResponseModel(
    @ApiModelProperty(value = "Transaction ID returned by Payment SDK")
    val id: String?,

    @ApiModelProperty(value = "Amount")
    val amount: Int?,

    @ApiModelProperty(value = "Currency")
    val currency: String?,

    @ApiModelProperty(value = "Transaction Status")
    val status: TransactionStatus?,

    @ApiModelProperty(value = "Transaction Action")
    val action: TransactionAction?,

    @ApiModelProperty(value = "Additional Info")
    val additionalInfo: String?
)
