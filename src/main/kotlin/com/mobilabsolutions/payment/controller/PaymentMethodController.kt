package com.mobilabsolutions.payment.controller

import com.mobilabsolutions.payment.model.request.CreatePaymentMethodRequestModel
import com.mobilabsolutions.payment.service.PaymentMethodService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import javax.validation.Valid

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@RestController
@RequestMapping(PaymentMethodController.BASE_URL)
@Validated
class PaymentMethodController(private val paymentMethodService: PaymentMethodService) {
    companion object {
        const val BASE_URL = "payment-method"
        const val DELETE_PAYMENT_METHOD_URL = "/{Payment-Method-Id}"
        const val GET_PAYMENT_METHODS_URL = "/{User-Id}"
    }

    @ApiOperation(value = "Create a Payment method for payment operations")
    @ApiResponses(
        ApiResponse(code = 201, message = "Successfully created a Payment method"),
        ApiResponse(code = 400, message = "Failed to create Payment method")
    )
    @RequestMapping(method = [RequestMethod.POST],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createPaymentMethod(
        @Valid @RequestBody request: CreatePaymentMethodRequestModel
    ) = paymentMethodService.createPaymentMethod(request)

    @ApiOperation(value = "Delete a Payment method")
    @ApiResponses(
        ApiResponse(code = 204, message = "Successfully deleted a Payment method"),
        ApiResponse(code = 400, message = "Failed to delete Payment method")
    )
    @RequestMapping(DELETE_PAYMENT_METHOD_URL, method = [RequestMethod.DELETE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAlias(
        @PathVariable("Payment-Method-Id") paymentMethodId: String
    ) = paymentMethodService.deletePaymentMethod(paymentMethodId)

    @ApiOperation(value = "Get Payment methods")
    @ApiResponses(
        ApiResponse(code = 200, message = "Successfully returned Payment methods"),
        ApiResponse(code = 400, message = "Failed to get Payment methods")
    )
    @RequestMapping(GET_PAYMENT_METHODS_URL, method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getPaymentMethods(
        @PathVariable("User-Id") userId: String
    ) = paymentMethodService.findAllPaymentMethodsForUser(userId)
}
