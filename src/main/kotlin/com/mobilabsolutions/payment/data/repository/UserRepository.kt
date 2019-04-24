package com.mobilabsolutions.payment.data.repository

import com.mobilabsolutions.payment.data.domain.User

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
interface UserRepository : BaseRepository<User, String> {
    fun getFirstById(id: String): User?
}
