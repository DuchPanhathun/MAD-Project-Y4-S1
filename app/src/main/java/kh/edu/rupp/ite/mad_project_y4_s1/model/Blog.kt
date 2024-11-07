package kh.edu.rupp.ite.mad_project_y4_s1.model

import android.os.Parcelable
<<<<<<< HEAD
=======
import com.google.firebase.Timestamp
>>>>>>> origin/thun
import kotlinx.parcelize.Parcelize

@Parcelize
data class Blog(
    val title: String = "",
    val detail: String = "",
    val additionalDetails: String = "",
    val coverImage: String = "",
<<<<<<< HEAD
    val title: String = "",
    val detail: String = "",
    val additionalPhotos: List<String> = listOf(),
    val additionalDetails: String = ""
=======
    val additionalPhotos: List<String> = listOf(),
    val createdAt: Timestamp? = null
>>>>>>> origin/thun
) : Parcelable
