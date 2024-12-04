package kh.edu.rupp.ite.mad_project_y4_s1.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kh.edu.rupp.ite.mad_project_y4_s1.model.PurchasedItem

class PurchasedViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val _purchased = MutableLiveData<List<PurchasedItem>>()
    val purchased: LiveData<List<PurchasedItem>> = _purchased

    init {
        fetchPurchased()
    }

    private fun fetchPurchased() {
        val userId = auth.currentUser?.uid ?: return
        
        firestore.collection("users")
            .document(userId)
            .collection("purchased")
            .orderBy("purchaseDate")  // Sort by purchase date
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("PurchasedViewModel", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val purchasedList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(PurchasedItem::class.java)
                } ?: emptyList()

                _purchased.value = purchasedList
                Log.d("PurchasedViewModel", "Fetched purchased items: $purchasedList")
            }
    }

    fun addPurchase(purchasedItem: PurchasedItem) {
        val userId = auth.currentUser?.uid ?: return
        
        val itemToAdd = purchasedItem.copy(quantity = purchasedItem.quantity.coerceAtLeast(1))
        
        firestore.collection("users")
            .document(userId)
            .collection("purchased")
            .add(itemToAdd)
            .addOnSuccessListener { documentReference ->
                Log.d("PurchasedViewModel", "Purchase added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("PurchasedViewModel", "Error adding purchase", e)
            }
    }
} 