package kh.edu.rupp.ite.mad_project_y4_s1.api

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kh.edu.rupp.ite.mad_project_y4_s1.model.Item

class ItemsApi {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getItems(): List<Item> {
        val snapshot = db.collection("items").get().await()
        return snapshot.toObjects(Item::class.java)
    }
} 