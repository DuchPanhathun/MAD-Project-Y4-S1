package kh.edu.rupp.ite.mad_project_y4_s1.api

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import kh.edu.rupp.ite.mad_project_y4_s1.model.Blog

class BlogApi {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getBlogs(): List<Blog> {
        try {
            val snapshot = db.collection("blogPosts")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            return snapshot.toObjects(Blog::class.java)
        } catch (e: Exception) {
            throw Exception("Failed to fetch blogs: ${e.message}")
        }
    }

    suspend fun getBlogById(blogId: String): Blog? {
        try {
            val document = db.collection("blogPosts")
                .document(blogId)
                .get()
                .await()
            return document.toObject(Blog::class.java)
        } catch (e: Exception) {
            throw Exception("Failed to fetch blog: ${e.message}")
        }
    }
} 