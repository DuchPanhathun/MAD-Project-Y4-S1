package kh.edu.rupp.ite.mad_project_y4_s1.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Blog(
    val title: String = "",
    val detail: String = "",
    val additionalDetails: String = "",
    val coverImage: String = "",
    val additionalPhotos: List<String> = listOf(),
    val createdAt: Timestamp? = null
) : Parcelable
