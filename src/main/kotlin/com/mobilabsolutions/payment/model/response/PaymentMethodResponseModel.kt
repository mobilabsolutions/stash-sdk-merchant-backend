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

    @ApiModelProperty(value = "Payment method alias - card number or email for PayPal", example = "VISA-1111")
    val alias: String?,

    @ApiModelProperty(value = "Payment method type", example = "SEPA")
    val type: PaymentMethodType?
)
