package kh.edu.rupp.ite.mad_project_y4_s1.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kh.edu.rupp.ite.mad_project_y4_s1.model.User
import com.google.firebase.Timestamp

class AuthApi {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun signIn(email: String, password: String): FirebaseUser? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user
    }

    suspend fun signUp(email: String, password: String): FirebaseUser? {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user?.let { firebaseUser ->
            val user = hashMapOf(
                "uid" to firebaseUser.uid,
                "email" to email,
                "firstName" to "",
                "lastName" to "",
                "address" to "",
                "city" to "",
                "phoneNumber" to "",
                "admin" to false,
                "createdAt" to Timestamp.now(),
                "paymentMethod" to null
            )
            db.collection("users").document(firebaseUser.uid)
                .set(user)
                .await()
        }
        return result.user
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut() {
        auth.signOut()
    }
} 