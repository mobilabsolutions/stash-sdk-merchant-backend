package com.mobilabsolutions.payment.model.response

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@ApiModel(value = "List of Payment Methods Model")
data class PaymentMethodListResponseModel(
    @ApiModelProperty(value = "List of payment methods")
    val paymentMethods: MutableList<PaymentMethodResponseModel> = mutableListOf()
)
