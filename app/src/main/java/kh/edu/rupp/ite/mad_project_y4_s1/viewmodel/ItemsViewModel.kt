package kh.edu.rupp.ite.mad_project_y4_s1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiResponse
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiState
import kh.edu.rupp.ite.mad_project_y4_s1.model.Item
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ItemsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _itemsState = MutableLiveData<ApiResponse<List<Item>>>(ApiResponse(ApiState.LOADING))
    val itemsState: LiveData<ApiResponse<List<Item>>> = _itemsState

    private val _totalQuantity = MutableLiveData<Int>()
    val totalQuantity: LiveData<Int> = _totalQuantity

    private var currentItems = listOf<Item>()
    // Add sorting enum
    enum class SortOrder {
        NEWEST,
        OLDEST
    }

    private var currentSortOrder = SortOrder.NEWEST

    init {
        fetchItems()
    }

    private fun fetchItems(sortOrder: SortOrder = SortOrder.NEWEST) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("items").get().await()
                currentItems = snapshot.toObjects(Item::class.java)
                // Sort items based on timestamp
                val sortedItems = when (sortOrder) {
                    SortOrder.NEWEST -> currentItems.sortedByDescending { it.timestamp }
                    SortOrder.OLDEST -> currentItems.sortedBy { it.timestamp }
                }
                _itemsState.value = ApiResponse(ApiState.SUCCESS, data = sortedItems)

                // Calculate total quantity
                val total = sortedItems.sumOf { it.getQuantityAsInt() }
                _totalQuantity.value = total
            } catch (e: Exception) {
                _itemsState.value = ApiResponse(ApiState.ERROR, error = e.message ?: "Unknown error occurred")
            }
        }
    }

    fun sortItems(sortOrder: SortOrder) {
        val sortedItems = when (sortOrder) {
            SortOrder.NEWEST -> currentItems.sortedByDescending { it.timestamp }
            SortOrder.OLDEST -> currentItems.sortedBy { it.timestamp }
        }
        _itemsState.value = ApiResponse(ApiState.SUCCESS, data = sortedItems)
    }

    fun toggleSortOrder() {
        currentSortOrder = when (currentSortOrder) {
            SortOrder.NEWEST -> SortOrder.OLDEST
            SortOrder.OLDEST -> SortOrder.NEWEST
        }
        sortItems(currentSortOrder)
    }

    fun getCurrentSortOrder(): SortOrder {
        return currentSortOrder
    }
}

