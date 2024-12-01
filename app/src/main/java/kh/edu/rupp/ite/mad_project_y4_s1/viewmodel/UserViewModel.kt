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
import kh.edu.rupp.ite.mad_project_y4_s1.model.User

class UserViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _userState = MutableStateFlow<ApiResponse<User>>(ApiResponse(ApiState.LOADING))
    val userState: StateFlow<ApiResponse<User>> = _userState

    suspend fun getCurrentUser(uid: String) {
        try {
            _userState.emit(ApiResponse(ApiState.LOADING))
            val document = db.collection("users").document(uid).get().await()
            if (document.exists()) {
                val user = document.toObject(User::class.java)
                user?.let {
                    _userState.emit(ApiResponse(ApiState.SUCCESS, data = it))
                }
            } else {
                _userState.emit(ApiResponse(ApiState.ERROR, error = "User not found"))
            }
        } catch (e: Exception) {
            _userState.emit(ApiResponse(ApiState.ERROR, error = e.message))
        }
    }

    suspend fun updateUser(user: User) {
        try {
            _userState.emit(ApiResponse(ApiState.LOADING))
            db.collection("users").document(user.uid)
                .set(user)
                .await()
            _userState.emit(ApiResponse(ApiState.SUCCESS, data = user))
        } catch (e: Exception) {
            _userState.emit(ApiResponse(ApiState.ERROR, error = e.message))
        }
    }
} 