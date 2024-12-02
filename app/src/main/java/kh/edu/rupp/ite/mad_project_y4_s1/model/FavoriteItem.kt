package kh.edu.rupp.ite.mad_project_y4_s1.model

import com.google.firebase.firestore.DocumentId

data class FavoriteItem(
    @DocumentId
    val id: String = "",  // Firestore document ID
    val imageUrl: String = "",
    val brandName: String = "",
    val type: String = "",
    val price: String = "",
    val sizes: List<String> = listOf()
) 