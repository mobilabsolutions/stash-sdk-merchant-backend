package com.mobilabsolutions.payment.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author <a href="mailto:mohamed.osman@mobilabsolutions.com">Mohamed Osman</a>
 */
@ApiModel(value = "PayPal Data Model")
data class PayPalDataModel(
    @ApiModelProperty(value = "Email", example = "abc@gmail.com")
    val email: String
)
