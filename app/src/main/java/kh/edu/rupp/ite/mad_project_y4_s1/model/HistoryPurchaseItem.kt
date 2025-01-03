package kh.edu.rupp.ite.mad_project_y4_s1.model

data class HistoryPurchaseItem(
    val id: String = "",
    val imageUrl: String = "",
    val brandName: String = "",
    val type: String = "",
    val price: String = "",
    val selectedSize: String = "",
    val selectedColor: String = "",
    val purchaseDate: Long = System.currentTimeMillis(),
    val quantity: Int = 1,
    val originalPrice: Double = 0.0,
    val discountPercentage: Double = 0.0,
    val finalPrice: Double = 0.0,
    val appliedPromoCode: String = ""
) 