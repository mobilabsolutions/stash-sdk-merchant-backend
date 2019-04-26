package com.mobilabsolutions.payment.paymentsdk.model.request

import com.mobilabsolutions.payment.paymentsdk.model.PaymentDataModel
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@ApiModel(value = "Authorization Request")
data class PaymentSdkAuthorizationRequestModel(
    @ApiModelProperty("Alias ID", example = "JipfjLKL6BkTIREaRGyX")
    @field:NotNull
    val aliasId: String?,

    @ApiModelProperty("Payment data")
    @field:Valid
    @field:NotNull
    val paymentData: PaymentDataModel?,

    @ApiModelProperty("Purchase ID", example = "132")
    val purchaseId: String?,

    @ApiModelProperty("Customer ID", example = "122")
    val customerId: String?

)
