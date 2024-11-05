package kh.edu.rupp.ite.mad_project_y4_s1.model

<<<<<<< HEAD
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


=======
data class ApiResponse<T>(
    val status: ApiState,
    val data: T? = null,
    val error: String? = null
)

>>>>>>> 0c597ef (fix stash)
enum class ApiState {
    LOADING,
    SUCCESS,
    ERROR
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
>>>>>>> 215843c (fix stash)
=======
>>>>>>> 042a0ba (...)
=======
>>>>>>> f157459 (Blog post)
<<<<<<< HEAD:app/src/main/java/kh/edu/rupp/ite/mad_project_y4_s1/model/ApiResponse.kt
}

=======
}
>>>>>>> 0d79057 (..):app/src/main/java/kh/edu/rupp/ite/mad_project_y4_s1/model/ ApiResponse.kt
<<<<<<< HEAD
>>>>>>> 221b825 (..)
=======
=======
} 
>>>>>>> 0c597ef (fix stash)
<<<<<<< HEAD
>>>>>>> 215843c (fix stash)
=======
=======
=======
>>>>>>> 73a4b5e (Blog post)
} 
=======
}
>>>>>>> cd6fae2 (...)
<<<<<<< HEAD
>>>>>>> 05d8f46 (...)
<<<<<<< HEAD
>>>>>>> 042a0ba (...)
=======
=======
=======
}

>>>>>>> 27b5cc5 (Blog post)
>>>>>>> 73a4b5e (Blog post)
>>>>>>> f157459 (Blog post)
