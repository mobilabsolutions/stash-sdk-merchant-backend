package com.mobilabsolutions.payment.common.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@Configuration
@EnableSwagger2
class WebConfiguration : WebMvcConfigurer {
    companion object {
        val DEFAULT_TIMEOUT = TimeUnit.SECONDS.toMillis(5).toInt()
    }

    @Autowired
    private lateinit var jsonConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var jsonMapper: ObjectMapper

    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
        .apiInfo(
            ApiInfoBuilder()
                .title("Payment SDK Merchant Backend")
                .description("Project Wiki: https://github.com/mobilabsolutions/payment-sdk-wiki-open")
                .version("1.0")
                .contact(
                    Contact(
                        "MobiLab Solutions GmbH",
                        "https://mobilabsolutions.com/",
                        "info@mobilabsolutions.com"
                    )
                )
                .build()
        )
        .useDefaultResponseMessages(false)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.mobilabsolutions.payment.controller"))
        .build()

    @Bean
    fun restTemplate(): RestTemplate {
        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.setConnectTimeout(DEFAULT_TIMEOUT)
        requestFactory.setReadTimeout(DEFAULT_TIMEOUT)
        val restTemplate = RestTemplate(requestFactory)
        restTemplate.messageConverters.forEach { this.configureConverters(it) }
        return restTemplate
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(jsonConverter)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }

    private fun configureConverters(messageConverter: HttpMessageConverter<*>) {
        if (messageConverter is MappingJackson2HttpMessageConverter) {
            messageConverter.defaultCharset = Charset.forName("UTF-8")
            messageConverter.objectMapper = jsonMapper
        }
        if (messageConverter is StringHttpMessageConverter) {
            messageConverter.defaultCharset = Charset.forName("UTF-8")
        }
    }
}
