package kh.edu.rupp.ite.mad_project_y4_s1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import kh.edu.rupp.ite.mad_project_y4_s1.api.BlogApi
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiResponse
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiState
import kh.edu.rupp.ite.mad_project_y4_s1.model.Blog

class BlogViewModel : ViewModel() {
    private val blogApi = BlogApi()
    private val _blogsState = MutableLiveData<ApiResponse<List<Blog>>>()
    val blogsState: LiveData<ApiResponse<List<Blog>>> = _blogsState

    private val _selectedBlog = MutableLiveData<Blog>()
    val selectedBlog: LiveData<Blog> = _selectedBlog

    init {
        fetchBlogs()
    }

    private fun fetchBlogs() {
        viewModelScope.launch {
            _blogsState.value = ApiResponse(ApiState.LOADING)
            try {
<<<<<<< HEAD
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
=======
                val blogs = blogApi.getBlogs()
                _blogsState.value = ApiResponse(ApiState.SUCCESS, blogs)
>>>>>>> origin/thun
            } catch (e: Exception) {
                _blogsState.value = ApiResponse(ApiState.ERROR, error = e.message ?: "Unknown error occurred")
            }
        }
    }

    fun setSelectedBlog(blog: Blog) {
        _selectedBlog.value = blog
    }
}
