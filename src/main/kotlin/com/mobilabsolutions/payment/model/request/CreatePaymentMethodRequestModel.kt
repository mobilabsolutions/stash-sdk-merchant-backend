package com.mobilabsolutions.payment.model.request

import com.mobilabsolutions.payment.data.enum.PaymentMethodType
import com.mobilabsolutions.payment.model.CreditCardDataModel
import com.mobilabsolutions.payment.model.PayPalDataModel
import com.mobilabsolutions.payment.model.SepaDataModel
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

    @ApiModelProperty(value = "Payment method type", example = "SEPA")
    @field:Enumerated(EnumType.STRING)
    @field:NotNull
    val type: PaymentMethodType?,

    @ApiModelProperty(value = "User ID", example = "X0yaCDH84WmdDMWxaqGY")
    @field:NotNull
    val userId: String?,

    @ApiModelProperty(value = "Credit card configuration")
    val ccData: CreditCardDataModel,

    @ApiModelProperty(value = "PayPal configuration")
    val payPalData: PayPalDataModel,

    @ApiModelProperty(value = "SEPA configuration")
    val sepaData: SepaDataModel
)
