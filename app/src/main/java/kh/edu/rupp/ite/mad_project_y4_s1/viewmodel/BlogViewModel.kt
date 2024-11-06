package kh.edu.rupp.ite.mad_project_y4_s1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kh.edu.rupp.ite.mad_project_y4_s1.api.BlogApi
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiResponse
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiState
import kh.edu.rupp.ite.mad_project_y4_s1.model.Blog

class BlogViewModel : ViewModel() {
    private val blogApi = BlogApi()
    private val _blogsState = MutableStateFlow<ApiResponse<List<Blog>>>(ApiResponse(ApiState.LOADING))
    val blogsState: StateFlow<ApiResponse<List<Blog>>> = _blogsState

    init {
        fetchBlogs()
    }

    private fun fetchBlogs() {
        viewModelScope.launch {
            _blogsState.emit(ApiResponse(ApiState.LOADING))
            try {
                val blogs = blogApi.getBlogs()
                _blogsState.emit(ApiResponse(ApiState.SUCCESS, blogs))
            } catch (e: Exception) {
                _blogsState.emit(ApiResponse(ApiState.ERROR, error = e.message ?: "Unknown error occurred"))
            }
        }
    }
}
