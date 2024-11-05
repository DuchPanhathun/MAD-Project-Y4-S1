package kh.edu.rupp.ite.mad_project_y4_s1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Blog(
    val coverImage: String = "",
    val title: String = "",
    val detail: String = "",
    val additionalPhotos: List<String> = listOf(),
    val additionalDetails: String = ""
) : Parcelable
