package kh.edu.rupp.ite.mad_project_y4_s1.model

import com.google.android.gms.common.api.Status
import org.jetbrains.annotations.ApiStatus

data class ApiResponse<T>(
    val status : ApiState,
    val data: T? = null,
    val error: String? = null,
)


enum class ApiState {
    LOADING,
    SUCCESS,
    ERROR
}