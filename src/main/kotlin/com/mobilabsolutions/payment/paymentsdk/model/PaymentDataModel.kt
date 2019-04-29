package com.mobilabsolutions.payment.paymentsdk.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@ApiModel(value = "Payment Data Model")
data class PaymentDataModel(
    @ApiModelProperty(value = "Amount in smallest currency unit (e.g. cent)", example = "300")
    @field:NotNull
    val amount: Int?,

    @ApiModelProperty(value = "Currency", example = "EUR")
    @field:Size(min = 3, max = 3)
    @field:NotNull
    val currency: String?,

    @ApiModelProperty(value = "Reason", example = "Payment for dinner")
    @field:NotNull
    val reason: String?
)
