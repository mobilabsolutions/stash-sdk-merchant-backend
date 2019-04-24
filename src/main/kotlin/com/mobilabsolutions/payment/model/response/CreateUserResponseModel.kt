package com.mobilabsolutions.payment.model.response

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@ApiModel(value = "Create User Response")
data class CreateUserResponseModel(
    @ApiModelProperty(value = "User ID")
    val userId: String?
)
