package com.mobilabsolutions.payment.paymentsdk.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@Configuration
@ConfigurationProperties(prefix = "payment.sdk")
class PaymentSdkConfiguration {
    lateinit var authorizationUrl: String
    lateinit var deleteAliasUrl: String
    lateinit var merchantSecretKey: String
    lateinit var testMode: String
}
