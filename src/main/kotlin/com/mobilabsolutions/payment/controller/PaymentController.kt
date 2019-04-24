package com.mobilabsolutions.payment.controller

import com.mobilabsolutions.payment.model.request.PaymentRequestModel
import com.mobilabsolutions.payment.service.TransactionService
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
import javax.validation.Valid

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@RestController
@RequestMapping(PaymentController.BASE_URL)
@Validated
class PaymentController(private val transactionService: TransactionService) {
    companion object {
        const val BASE_URL = "authorization"
    }

    @ApiOperation(value = "Authorize transaction")
    @ApiResponses(
        ApiResponse(code = 201, message = "Successfully authorized transaction"),
        ApiResponse(code = 400, message = "Failed to authorize transaction"),
        ApiResponse(code = 404, message = "Not found")
    )
    @RequestMapping(
        method = [RequestMethod.PUT],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun authorizeTransaction(
        @Valid @RequestBody request: PaymentRequestModel
    ) = transactionService.authorize(request)
}
