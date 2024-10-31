package kh.edu.rupp.ite.mad_project_y4_s1.api

sealed class ApiState<out T> {
    data class Success<T>(val data: T): ApiState<T>()
    data class Error(val message: String): ApiState<Nothing>()
    object Loading : ApiState<Nothing>()
}