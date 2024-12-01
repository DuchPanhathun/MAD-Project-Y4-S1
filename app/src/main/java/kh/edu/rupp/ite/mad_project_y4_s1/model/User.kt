package kh.edu.rupp.ite.mad_project_y4_s1.model

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val address: String = "",
    val city: String = "",
    val phoneNumber: String = "",
    val isAdmin: Boolean = false,
    val createdAt: Timestamp = Timestamp.now(),
    val paymentMethod: PaymentMethod? = null
)

data class PaymentMethod(
    val cardNumber: String = "",
    val cvv: String = "",
    val expMonth: String = "",
    val expYear: String = "",
    val nameOnCard: String = ""
) 