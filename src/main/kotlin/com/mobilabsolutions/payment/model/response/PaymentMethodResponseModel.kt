package com.mobilabsolutions.payment.model.response

import com.mobilabsolutions.payment.data.enum.PaymentMethodType
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@ApiModel(value = "Payment Method Response")
data class PaymentMethodResponseModel(
    @ApiModelProperty(value = "Payment method ID", example = "jsnxcbjkmdmckd")
    val paymentMethodId: String?,

    @ApiModelProperty(value = "Credit card expiry month", example = "01")
    val ccExpiryMonth: String?,

    @ApiModelProperty(value = "Credit card expiry year", example = "19")
    val ccExpiryYear: String?,

    @ApiModelProperty(value = "Card type", example = "VISA")
    val cardType: String?,

    @ApiModelProperty(value = "Card mask - last 4 digits", example = "1111")
    val cardMask: String?,

    @ApiModelProperty(value = "Payment method type", example = "SEPA")
    val type: PaymentMethodType?
)
