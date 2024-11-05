package kh.edu.rupp.ite.mad_project_y4_s1.model

import com.google.android.gms.common.api.Status
import org.jetbrains.annotations.ApiStatus

data class ApiResponse<T>(
<<<<<<< HEAD:app/src/main/java/kh/edu/rupp/ite/mad_project_y4_s1/model/ApiResponse.kt
    val status: ApiState,
=======
    val status : ApiState,
>>>>>>> 0d79057 (..):app/src/main/java/kh/edu/rupp/ite/mad_project_y4_s1/model/ ApiResponse.kt
    val data: T? = null,
    val error: String? = null,
)


enum class ApiState {
    LOADING,
    SUCCESS,
    ERROR
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
} 
=======
}
>>>>>>> cd6fae2 (...)
=======
}

>>>>>>> 27b5cc5 (Blog post)
=======
<<<<<<< HEAD:app/src/main/java/kh/edu/rupp/ite/mad_project_y4_s1/model/ApiResponse.kt
}

=======
}
>>>>>>> 0d79057 (..):app/src/main/java/kh/edu/rupp/ite/mad_project_y4_s1/model/ ApiResponse.kt
>>>>>>> 221b825 (..)
