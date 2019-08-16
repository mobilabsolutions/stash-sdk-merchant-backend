package com.mobilabsolutions.payment.controller

import com.mobilabsolutions.payment.model.request.MerchantNotificationsRequestModel
import com.mobilabsolutions.payment.service.TransactionService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(NotificationController.BASE_URL)
@Validated
class NotificationController(private val transactionService: TransactionService) {
    companion object {
        const val BASE_URL = "notification"
    }

    @ApiOperation(value = "Create transaction from notifications")
    @ApiResponses(
        ApiResponse(code = 201, message = "Successfully created transaction notification"),
        ApiResponse(code = 400, message = "Failed to create transaction notification"),
        ApiResponse(code = 401, message = "Unauthorized access"),
        ApiResponse(code = 404, message = "Not found")
    )
    @RequestMapping(
        method = [RequestMethod.PUT],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun createTransactionNotification(
        @Valid @ApiParam(name = "Merchant-Notification-Info", value = "Merchant Notification Model") @RequestBody merchantNotificationListRequestModel: MerchantNotificationsRequestModel
    ) = transactionService.createNotificationTransactionRecord(merchantNotificationListRequestModel)
}
