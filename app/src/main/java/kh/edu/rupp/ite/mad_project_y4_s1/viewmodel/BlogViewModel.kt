package kh.edu.rupp.ite.mad_project_y4_s1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiResponse
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiState

class BlogViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _blogsState = MutableStateFlow<ApiResponse<List<Blog>>>(ApiResponse(ApiState.LOADING))
    val blogsState: StateFlow<ApiResponse<List<Blog>>> = _blogsState

    init {
        fetchBlogs()
    }

    private fun fetchBlogs() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("blogPosts").get().await()
                val blogs = snapshot.documents.mapNotNull { doc ->
                    Blog(
                        coverImage = doc.getString("coverImage") ?: "",
                        title = doc.getString("title") ?: "",
                        detail = doc.getString("detail") ?: "",
                        additionalPhotos = (doc.get("additionalPhotos") as? List<String>) ?: listOf(),
                        additionalDetails = doc.getString("additionalDetails") ?: ""
                    )
                }
                _blogsState.emit(ApiResponse(ApiState.SUCCESS, data = blogs))
            } catch (e: Exception) {
                _blogsState.emit(ApiResponse(ApiState.ERROR, error = e.message ?: "Unknown error occurred"))
            }
        }
    }
}
