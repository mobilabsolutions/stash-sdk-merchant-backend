package com.mobilabsolutions.payment.data.repository

import com.mobilabsolutions.payment.data.domain.PaymentMethod

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
interface PaymentMethodRepository : BaseRepository<PaymentMethod, String> {
    fun getFirstById(id: String): PaymentMethod?

    fun findAllByUserIdAndActive(userId: String, active: Boolean): List<PaymentMethod>
}
