package com.mobilabsolutions.payment.common.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@Configuration
@EnableResourceServer
class ResourceServerConfiguration : ResourceServerConfigurerAdapter() {

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources!!.resourceId(RESOURCE_ID)
    }

    override fun configure(http: HttpSecurity) {
        http.requestMatchers().anyRequest().and().authorizeRequests()
            .antMatchers(*PERMITTED_PATTERNS, *SWAGGER_PATTERNS).permitAll()
    }

    companion object {
        private const val RESOURCE_ID = "payment-sdk-merchant-rest-api"
        private val SWAGGER_PATTERNS = arrayOf("/swagger-ui.html", "/api-docs/**", "/webjars/**", "/v2/**", "/swagger-resources/**")
        private val PERMITTED_PATTERNS = arrayOf("/actuator/**", "/merchant/**")
    }
}
