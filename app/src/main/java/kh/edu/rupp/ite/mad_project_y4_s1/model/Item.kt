package kh.edu.rupp.ite.mad_project_y4_s1.model

import com.google.firebase.firestore.PropertyName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
    @PropertyName("images") val images: List<String> = listOf()
) : Parcelable {
    // Required no-argument constructor for Firestore
    constructor() : this("", "", "", listOf(), listOf(), "", "", listOf(), "", "", listOf())
}