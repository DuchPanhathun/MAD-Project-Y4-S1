package kh.edu.rupp.ite.mad_project_y4_s1.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import kotlinx.coroutines.tasks.await
import kh.edu.rupp.ite.mad_project_y4_s1.model.AuthResult
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException

class AuthApi {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    
    // Add these properties to track login attempts
    companion object {
        private const val MAX_LOGIN_ATTEMPTS = 5
        private const val COOLDOWN_DURATION = 300000L // 5 minutes in milliseconds
        private var loginAttempts = 0
        private var lastLoginAttemptTime = 0L
    }

    suspend fun signIn(email: String, password: String): AuthResult {
        val currentTime = System.currentTimeMillis()
        
        // Check if we're in cooldown period
        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            val timeElapsed = currentTime - lastLoginAttemptTime
            if (timeElapsed < COOLDOWN_DURATION) {
                val remainingTime = (COOLDOWN_DURATION - timeElapsed) / 1000 // Convert to seconds
                return AuthResult(
                    isSuccess = false,
                    error = "Please wait ${remainingTime} seconds before trying again."
                )
            } else {
                // Reset attempts after cooldown period
                loginAttempts = 0
            }
        }

        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                // Reset attempts on successful login
                loginAttempts = 0
                AuthResult(isSuccess = true)
            } else {
                handleFailedAttempt(currentTime)
                AuthResult(isSuccess = false, error = "Login failed")
            }
        } catch (e: FirebaseAuthException) {
            handleFailedAttempt(currentTime)
            when {
                e.message?.contains("unusual activity") == true -> {
                    AuthResult(
                        isSuccess = false,
                        error = "Too many login attempts. Please wait ${COOLDOWN_DURATION/1000} seconds before trying again."
                    )
                }
                e.message?.contains("password is invalid") == true -> {
                    AuthResult(isSuccess = false, error = "Invalid password")
                }
                e.message?.contains("no user record") == true -> {
                    AuthResult(isSuccess = false, error = "Email not found")
                }
                else -> AuthResult(isSuccess = false, error = e.message)
            }
        } catch (e: Exception) {
            handleFailedAttempt(currentTime)
            AuthResult(isSuccess = false, error = "An unexpected error occurred")
        }
    }

    private fun handleFailedAttempt(currentTime: Long) {
        loginAttempts++
        lastLoginAttemptTime = currentTime
    }

    // Add a method to check remaining cooldown time
    fun getRemainingCooldownTime(): Long {
        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            val timeElapsed = System.currentTimeMillis() - lastLoginAttemptTime
            if (timeElapsed < COOLDOWN_DURATION) {
                return (COOLDOWN_DURATION - timeElapsed) / 1000 // Return remaining seconds
            }
        }
        return 0
    }

    // Add a method to reset login attempts
    fun resetLoginAttempts() {
        loginAttempts = 0
        lastLoginAttemptTime = 0
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