package kh.edu.rupp.ite.mad_project_y4_s1.model

import com.google.firebase.firestore.DocumentId

data class PurchasedItem(
    @DocumentId
    val id: String = "",
    val imageUrl: String = "",
    val brandName: String = "",
    val type: String = "",
    val price: String = "",
    val selectedSize: String = "",
    val selectedColor: String = "",
    val purchaseDate: Long = System.currentTimeMillis(),
    var quantity: Int = 1,
    var appliedPromoCode: String = "",
    var discountPercentage: Double = 0.0,
    var availablePromoCodes: Map<String, Double> = mapOf()
) {
    fun applyPromoCode(code: String): Boolean {
        val discount = availablePromoCodes[code]
        return if (discount != null) {
            appliedPromoCode = code
            discountPercentage = discount / 100.0
            true
        } else {
            false
        }
    }

    fun removePromoCode() {
        appliedPromoCode = ""
        discountPercentage = 0.0
    }

    fun calculateDiscountedPrice(): Double {
        val originalPrice = price.removePrefix("$").toDouble()
        val totalOriginalPrice = originalPrice * quantity
        return totalOriginalPrice * (1 - discountPercentage)
    }

    fun calculateDiscount(): Double {
        val originalPrice = price.removePrefix("$").toDouble()
        val totalOriginalPrice = originalPrice * quantity
        return totalOriginalPrice * discountPercentage
    }
} 