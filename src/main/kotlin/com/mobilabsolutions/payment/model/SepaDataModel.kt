package com.mobilabsolutions.payment.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author <a href="mailto:mohamed.osman@mobilabsolutions.com">Mohamed Osman</a>
 */
@ApiModel(value = "SEPA Data Model")
class SepaDataModel(
    @ApiModelProperty(value = "IBAN", example = "DE00123456782599100004")
    val iban: String?
)
