package com.mobilabsolutions.payment.controller

import com.mobilabsolutions.payment.service.UserService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@RestController
@RequestMapping(UserController.BASE_URL)
@Validated
class UserController(private val userService: UserService) {
    companion object {
        const val BASE_URL = "user"
    }

    @ApiOperation(value = "Create an User for payment operations")
    @ApiResponses(
        ApiResponse(code = 201, message = "Successfully created an User")
    )
    @RequestMapping(method = [RequestMethod.POST],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser() = userService.createUser()
}
