package kh.edu.rupp.ite.mad_project_y4_s1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiResponse
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiState
import kh.edu.rupp.ite.mad_project_y4_s1.model.Item

class GridItemsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _itemsState = MutableLiveData<ApiResponse<List<Item>>>(ApiResponse(ApiState.LOADING))
    val itemsState: LiveData<ApiResponse<List<Item>>> = _itemsState

    init {
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("items")
                    .limit(4)  // Changed from 20 to 4
                    .get()
                    .await()
                val items = snapshot.toObjects(Item::class.java)
                _itemsState.value = ApiResponse(ApiState.SUCCESS, data = items)
            } catch (e: Exception) {
                _itemsState.value = ApiResponse(ApiState.ERROR, error = e.message)
            }
        }
    }
} 