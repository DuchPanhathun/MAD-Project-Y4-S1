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
    val purchaseDate: Long = System.currentTimeMillis()
) 