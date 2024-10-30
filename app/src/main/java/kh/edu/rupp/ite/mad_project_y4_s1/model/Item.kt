package kh.edu.rupp.ite.mad_project_y4_s1.model

import com.google.firebase.firestore.PropertyName

data class Item(
    @PropertyName("brandName") val brandName: String = "",
    @PropertyName("type") val type: String = "",
    @PropertyName("price") val price: String = "",
    @PropertyName("sizes") val sizes: List<String> = listOf(),
    @PropertyName("images") val images: List<String> = listOf()
) {
    // Required no-argument constructor for Firestore
    constructor() : this("", "", "", listOf(), listOf())
}