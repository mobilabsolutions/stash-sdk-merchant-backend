package com.mobilabsolutions.payment.paymentsdk.model.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@ApiModel(value = "Payment SDK Error Response")
data class PaymentSdkErrorResponseModel(
    @ApiModelProperty(value = "Payment SDK error code", example = "3005")
    @JsonProperty(value = "error_code")
    val errorCode: String?,

    @ApiModelProperty(value = "Payment SDK error description", example = "Alias ID cannot be found.")
    @JsonProperty(value = "error_description")
    val errorDescription: String?
)
