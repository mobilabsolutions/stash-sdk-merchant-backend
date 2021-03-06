package com.mobilabsolutions.payment.data.domain

import com.mobilabsolutions.payment.data.enum.PaymentMethodType
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

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@Entity
@Table(name = "payment_method")
class PaymentMethod(
    @Id
    @Column(name = "id")
    var id: String?,

    @Column(name = "active")
    var active: Boolean = true,

    @Column(name = "alias_id")
    var aliasId: String?,

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "cc_data")
    var ccData: String?,

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "paypal_data")
    var paypalData: String?,

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "sepa_data")
    var sepaData: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    var type: PaymentMethodType?,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = ForeignKey(name = "fk_user_paymentmethod"))
    var user: User
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

        other as PaymentMethod

        return this.id == other.id
    }
    override fun toString(): String {
        return "PaymentMethod [id=$id, active=$active, aliasId=$aliasId, ccData=$ccData, paypalData=$paypalData, sepaData=$sepaData, type=$type, user=$user]"
    }
}
