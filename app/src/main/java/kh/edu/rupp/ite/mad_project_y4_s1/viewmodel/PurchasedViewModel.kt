package kh.edu.rupp.ite.mad_project_y4_s1.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kh.edu.rupp.ite.mad_project_y4_s1.model.PurchasedItem
import kh.edu.rupp.ite.mad_project_y4_s1.model.Item

class PurchasedViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val _purchased = MutableLiveData<List<PurchasedItem>>()
    val purchased: LiveData<List<PurchasedItem>> = _purchased

    init {
        fetchPurchased()
    }

    private fun fetchPurchased() {
        val userId = auth.currentUser?.uid ?: return
        
        db.collection("users")
            .document(userId)
            .collection("purchased")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("PurchasedViewModel", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val purchasedList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(PurchasedItem::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                _purchased.value = purchasedList
            }
    }

    fun addPurchase(purchasedItem: PurchasedItem) {
        val userId = auth.currentUser?.uid ?: return
        
        val itemToAdd = purchasedItem.copy(quantity = purchasedItem.quantity.coerceAtLeast(1))
        
        db.collection("users")
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

    fun removeItem(itemId: String) {
        val userId = auth.currentUser?.uid ?: return
        
        db.collection("users")
            .document(userId)
            .collection("purchased")
            .document(itemId)
            .delete()
            .addOnSuccessListener {
                Log.d("PurchasedViewModel", "Item successfully removed")
            }
            .addOnFailureListener { e ->
                Log.w("PurchasedViewModel", "Error removing item", e)
            }
    }

    fun applyPromoCode(purchasedItemId: String, promoCode: String) {
        val userId = auth.currentUser?.uid ?: return
        
        // First, get the original item to check promo codes
        db.collection("items")
            .get()
            .addOnSuccessListener { documents ->
                var promoFound = false
                for (document in documents) {
                    val item = document.toObject(Item::class.java)
                    val discount = item.getDiscountForCode(promoCode)
                    
                    if (discount != null) {
                        promoFound = true
                        // Update the purchased item with the promo code
                        db.collection("users")
                            .document(userId)
                            .collection("purchased")
                            .document(purchasedItemId)
                            .update(mapOf(
                                "appliedPromoCode" to promoCode,
                                "discountPercentage" to (discount / 100.0)
                            ))
                            .addOnSuccessListener {
                                Log.d("PurchasedViewModel", "Promo code applied successfully")
                                // Refresh the purchased items
                                fetchPurchased()
                            }
                            .addOnFailureListener { e ->
                                Log.e("PurchasedViewModel", "Error applying promo code", e)
                            }
                        break
                    }
                }
                
                if (!promoFound) {
                    Log.d("PurchasedViewModel", "Invalid promo code")
                }
            }
            .addOnFailureListener { e ->
                Log.e("PurchasedViewModel", "Error checking promo code", e)
            }
    }

    fun removePromoCode(itemId: String) {
        val userId = auth.currentUser?.uid ?: return
        
        db.collection("users")
            .document(userId)
            .collection("purchased")
            .document(itemId)
            .update(
                mapOf(
                    "appliedPromoCode" to "",
                    "discountPercentage" to 0.0
                )
            )
            .addOnSuccessListener {
                Log.d("PurchasedViewModel", "Promo code removed successfully")
            }
            .addOnFailureListener { e ->
                Log.w("PurchasedViewModel", "Error removing promo code", e)
            }
    }
} 