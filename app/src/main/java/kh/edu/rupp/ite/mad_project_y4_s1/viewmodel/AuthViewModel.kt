package kh.edu.rupp.ite.mad_project_y4_s1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kh.edu.rupp.ite.mad_project_y4_s1.api.AuthApi
import kh.edu.rupp.ite.mad_project_y4_s1.model.AuthResult

class AuthViewModel : ViewModel() {
    private val authApi = AuthApi()
    private val _authState = MutableStateFlow<AuthResult?>(null)
    val authState: StateFlow<AuthResult?> = _authState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = authApi.signIn(email, password)
                _authState.emit(AuthResult(isSuccess = user != null))
            } catch (e: Exception) {
                _authState.emit(AuthResult(isSuccess = false, error = e.message))
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = authApi.signUp(email, password)
                _authState.emit(AuthResult(isSuccess = user != null))
            } catch (e: Exception) {
                _authState.emit(AuthResult(isSuccess = false, error = e.message))
            }
        }
    }
} 