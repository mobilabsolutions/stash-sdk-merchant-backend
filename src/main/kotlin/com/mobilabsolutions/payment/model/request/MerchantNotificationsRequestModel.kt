package com.mobilabsolutions.payment.model.request

import com.mobilabsolutions.payment.model.MerchantNotificationsModel
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author <a href="mailto:mohamed.osman@mobilabsolutions.com">Mohamed Osman</a>
 */
@ApiModel("Merchant notifications request model")
data class MerchantNotificationsRequestModel(
    @ApiModelProperty("Notifications")
    val notifications: MutableList<MerchantNotificationsModel>
)
