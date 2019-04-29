package com.mobilabsolutions.payment

import com.mobilabsolutions.payment.common.configuration.CommonConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(CommonConfiguration::class)
@SpringBootApplication(
    exclude = [
        HttpMessageConvertersAutoConfiguration::class
    ]
)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
