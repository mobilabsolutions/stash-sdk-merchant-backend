package com.mobilabsolutions.payment.model.request

import com.mobilabsolutions.payment.data.enum.PaymentMethodType
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotNull

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@ApiModel(value = "Create Payment Method Request")
data class CreatePaymentMethodRequestModel(
    @ApiModelProperty(value = "Alias ID returned by Payment SDK", example = "X0yaCDH84WmdDMWxaqGY")
    @field:NotNull
    val aliasId: String?,

    @ApiModelProperty(value = "Payment method alias - card number, or email for PayPal", example = "VISA-1111")
    @field:NotNull
    val alias: String,

    @ApiModelProperty(value = "Payment method type", example = "SEPA")
    @field:Enumerated(EnumType.STRING)
    @field:NotNull
    val type: PaymentMethodType?,

    @ApiModelProperty(value = "User ID", example = "X0yaCDH84WmdDMWxaqGY")
    @field:NotNull
    val userId: String?
)
