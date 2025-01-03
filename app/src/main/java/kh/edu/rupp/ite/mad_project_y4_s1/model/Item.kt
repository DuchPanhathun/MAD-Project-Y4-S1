package kh.edu.rupp.ite.mad_project_y4_s1.model

import com.google.firebase.firestore.PropertyName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class PromoCode(
    @PropertyName("code") val code: String = "",
    @PropertyName("discount") val discount: String = ""
) : Parcelable

@Parcelize
data class Item(
    @PropertyName("brandName") val brandName: String = "",
    @PropertyName("type") val type: String = "",
    @PropertyName("price") val price: String = "",
    @PropertyName("colors") val colors: List<String> = listOf(),
    @PropertyName("sizes") val sizes: List<String> = listOf(),
    @PropertyName("materialDetail") val materialDetail: String = "",
    @PropertyName("additionalCareDetails") val additionalCareDetails: String = "",
    @PropertyName("careDetails") val careDetails: List<String> = listOf(),
    @PropertyName("deliveryStartDate") val deliveryStartDate: String = "",
    @PropertyName("deliveryEndDate") val deliveryEndDate: String = "",
    @PropertyName("images") val images: List<String> = listOf(),
    @PropertyName("quantity") val quantity: String = "0",
    @PropertyName("timestamp") val timestamp: Long = 0L,
    @PropertyName("promoCodes") val promoCodes: List<PromoCode> = listOf()
) : Parcelable {
    // Required no-argument constructor for Firestore
    constructor() : this("", "", "", listOf(), listOf(), "", "", listOf(), "", "", listOf(), "0", 0L, listOf())

    // Helper function to get discount for a specific promo code
    fun getDiscountForCode(code: String): Double? {
        return promoCodes
            .find { it.code == code }
            ?.discount
            ?.toDoubleOrNull()
    }

    // Helper function to get quantity as Int
    fun getQuantityAsInt(): Int {
        return quantity.toIntOrNull() ?: 0
    }
}