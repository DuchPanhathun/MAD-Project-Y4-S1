package kh.edu.rupp.ite.mad_project_y4_s1.model

import com.google.firebase.firestore.PropertyName

data class BlogItem(
    @get:PropertyName("coverImages") val coverImage: String = "",
    @get:PropertyName("title") val title: String = ""
)
