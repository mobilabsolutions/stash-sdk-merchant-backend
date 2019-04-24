package com.mobilabsolutions.payment.data.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.dao.support.PersistenceExceptionTranslator
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.hibernate5.HibernateExceptionTranslator
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.validation.annotation.Validated
import javax.persistence.EntityManagerFactory

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@Validated
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
@ComponentScan(DataConfiguration.BASE_SCAN_PACKAGE)
@EnableJpaRepositories(basePackages = [(DataConfiguration.BASE_SCAN_PACKAGE)])
class DataConfiguration {

    companion object {
        const val BASE_SCAN_PACKAGE = "com.mobilabsolutions.payment"
    }

    /**
     * Declared the `PlatformTransactionManager` bean.
     *
     * @param emf the entity manager factory
     * @return the transaction manager
     */
    @Bean
    fun transactionManager(emf: EntityManagerFactory): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = emf
        return transactionManager
    }

    /**
     * Declares the `PersistenceExceptionTranslator` bean.
     *
     * @return the exception translator bean
     */
    @Bean
    fun hibernateExceptionTranslator(): PersistenceExceptionTranslator {
        return HibernateExceptionTranslator()
    }
}
