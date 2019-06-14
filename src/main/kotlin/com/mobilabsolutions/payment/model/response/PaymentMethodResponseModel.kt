package com.mobilabsolutions.payment.model.response

import com.mobilabsolutions.payment.data.enum.PaymentMethodType
import com.mobilabsolutions.payment.model.CreditCardDataModel
import com.mobilabsolutions.payment.model.PayPalDataModel
import com.mobilabsolutions.payment.model.SepaDataModel
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@ApiModel(value = "Payment Method Response")
data class PaymentMethodResponseModel(
    @ApiModelProperty(value = "Payment method ID", example = "jsnxcbjkmdmckd")
    val paymentMethodId: String?,

    @ApiModelProperty(value = "Payment method type", example = "SEPA")
    val type: PaymentMethodType?,

    @ApiModelProperty(value = "Credit card data")
    val ccData: CreditCardDataModel?,

    @ApiModelProperty(value = "PayPal data")
    val payPalData: PayPalDataModel?,

    @ApiModelProperty(value = "SEPA data")
    val sepaData: SepaDataModel?
)
