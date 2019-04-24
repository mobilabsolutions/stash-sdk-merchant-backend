package com.mobilabsolutions.payment.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@NoRepositoryBean
interface BaseRepository<T, ID : Serializable> : JpaRepository<T, ID>, JpaSpecificationExecutor<T>
