package kh.edu.rupp.ite.mad_project_y4_s1.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kh.edu.rupp.ite.mad_project_y4_s1.model.FavoriteItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val _favorites = MutableLiveData<List<FavoriteItem>>()
    val favorites: LiveData<List<FavoriteItem>> = _favorites

    init {
        fetchFavorites()
    }

    private fun fetchFavorites() {
        val userId = auth.currentUser?.uid ?: return
        
        firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("FavoritesViewModel", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val favoritesList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(FavoriteItem::class.java)
                } ?: emptyList()

                _favorites.value = favoritesList
                Log.d("FavoritesViewModel", "Fetched favorites: $favoritesList")
            }
    }

    fun addFavorite(favoriteItem: FavoriteItem) {
        val userId = auth.currentUser?.uid ?: return
        
        firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .add(favoriteItem)
            .addOnSuccessListener { documentReference ->
                Log.d("FavoritesViewModel", "Favorite added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("FavoritesViewModel", "Error adding favorite", e)
            }
    }

    fun removeFavorite(favoriteItem: FavoriteItem) {
        val userId = auth.currentUser?.uid ?: return
        
        firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .whereEqualTo("imageUrl", favoriteItem.imageUrl)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete()
                    Log.d("FavoritesViewModel", "Favorite deleted: ${document.id}")
                }
            }
            .addOnFailureListener { e ->
                Log.w("FavoritesViewModel", "Error removing favorite", e)
            }
    }
} 