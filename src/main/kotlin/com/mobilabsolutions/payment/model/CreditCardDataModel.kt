package com.mobilabsolutions.payment.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotNull

/**
 * @author <a href="mailto:mohamed.osman@mobilabsolutions.com">Mohamed Osman</a>
 */
@ApiModel(value = "Credit Card Data Model")
data class CreditCardDataModel(
    @ApiModelProperty(value = "Credit card expiry month", example = "01")
    val ccExpiryMonth: String?,

    @ApiModelProperty(value = "Credit card expiry year", example = "19")
    val ccExpiryYear: String?,

    @ApiModelProperty(value = "Credit card type", example = "VISA")
    @field:NotNull
    val ccType: String?,

    @ApiModelProperty(value = "Credit card mask - last 4 digits", example = "1111")
    @field:NotNull
    val ccMask: String?
)
