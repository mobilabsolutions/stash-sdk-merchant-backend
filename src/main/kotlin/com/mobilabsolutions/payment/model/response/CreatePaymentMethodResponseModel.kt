package com.mobilabsolutions.payment.model.response

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@ApiModel(value = "Create Payment Method Response")
data class CreatePaymentMethodResponseModel(
    @ApiModelProperty(value = "Payment method ID", example = "m9yaCDH84WmdDMWxaqGY")
    val paymentMethodId: String?
)
