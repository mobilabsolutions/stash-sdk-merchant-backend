package com.mobilabsolutions.payment.data.domain

import com.mobilabsolutions.payment.data.enum.TransactionAction
import com.mobilabsolutions.payment.data.enum.TransactionStatus
import org.hibernate.annotations.Type
import org.springframework.data.util.ProxyUtils
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Enumerated
import javax.persistence.EnumType
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn
import javax.persistence.ForeignKey
import javax.persistence.Lob

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@Entity
@Table(name = "transaction_record")
class Transaction(
    @Id
    @Column(name = "id")
    var id: String?,

    @Column(name = "amount")
    var amount: Int?,

    @Column(name = "currency")
    var currency: String?,

    @Column(name = "reason")
    var reason: String?,

    @Column(name = "transaction_id")
    var transactionId: String?,

    @Column(name = "idempotent_key")
    var idempotentKey: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    var action: TransactionAction?,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: TransactionStatus?,

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "payment_sdk_response")
    var paymentSdkResponse: String? = null,

    @ManyToOne
    @JoinColumn(name = "payment_method_id", nullable = false, foreignKey = ForeignKey(name = "fk_paymentmethod_transaction"))
    var paymentMethod: PaymentMethod
) : AutoGeneratedIdTimeAuditable() {

    override fun hashCode(): Int {
        var hash = 7
        hash = 97 * hash + Objects.hashCode(this.id)
        return hash
    }

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as Transaction

        return this.id == other.id
    }
    override fun toString(): String {
        return "Transaction [id=$id, amount=$amount, currency=$currency, paymentMethod=$paymentMethod]"
    }
}
