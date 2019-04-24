package com.mobilabsolutions.payment.model.request

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@ApiModel(value = "Create User Request")
data class CreateUserRequestModel(
    @ApiModelProperty(value = "User's first name", example = "Max")
    val firstName: String?,

    @ApiModelProperty(value = "User's last name", example = "Mustermann")
    val lastName: String?,

    @ApiModelProperty(value = "User's email", example = "max@mblb.net")
    val email: String?,

    @ApiModelProperty(value = "User's country", example = "DE")
    val country: String?
)
