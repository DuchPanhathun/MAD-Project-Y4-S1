package kh.edu.rupp.ite.mad_project_y4_s1.model

data class ApiResponse<T>(
    val status: ApiState,
    val data: T? = null,
    val error: String? = null
)

enum class ApiState {
    LOADING,
    SUCCESS,
    ERROR
<<<<<<< HEAD
<<<<<<< HEAD
} 
=======
}
>>>>>>> cd6fae2 (...)
=======
}

>>>>>>> 27b5cc5 (Blog post)
