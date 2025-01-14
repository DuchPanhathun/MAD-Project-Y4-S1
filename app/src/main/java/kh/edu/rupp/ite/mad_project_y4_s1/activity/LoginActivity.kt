package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    private var isPasswordVisible = false
    private var loginAttempts = 0
    private var lastLoginAttempt = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)

        val emailEditText: EditText = findViewById(R.id.editEmail)
        val passwordEditText: EditText = findViewById(R.id.editPassword)
        val passwordEyeIcon: ImageView = findViewById(R.id.eyeIcon)
        val loginButton: Button = findViewById(R.id.loginButton)
        val signUpText: TextView = findViewById(R.id.textLetter)

        // Handle password visibility toggle
        passwordEyeIcon.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(passwordEditText, isPasswordVisible, passwordEyeIcon)
        }

        lifecycleScope.launch {
            viewModel.authState.collect { result ->
                result?.let {
                    if (it.isSuccess) {
                        loginAttempts = 0 // Reset counter on success
                        Toast.makeText(baseContext, "Login successful.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorMessage = when {
                            it.error?.contains("unusual activity") == true -> 
                                "Too many login attempts. Please try again later or reset your password."
                            it.error?.contains("password") == true -> "Invalid password"
                            it.error?.contains("no user record") == true -> "Email not found"
                            else -> it.error ?: "Authentication failed."
                        }
                        Toast.makeText(baseContext, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        loginButton.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastLoginAttempt < 1000) { // Prevent rapid clicking
                Toast.makeText(this, "Please wait before trying again", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (loginAttempts >= 5) {
                    Toast.makeText(
                        this,
                        "Too many login attempts. Please try again later.",
                        Toast.LENGTH_LONG
                    ).show()
                    loginButton.isEnabled = false
                    return@setOnClickListener
                }

                loginAttempts++
                lastLoginAttempt = currentTime
                viewModel.signIn(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        signUpText.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun togglePasswordVisibility(editText: EditText, isVisible: Boolean, eyeIcon: ImageView) {
        if (isVisible) {
            // Set password visibility
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            eyeIcon.setImageResource(R.drawable.visible) // Open-eye icon
        } else {
            // Set password as hidden
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            eyeIcon.setImageResource(R.drawable.hide) // Closed-eye icon
        }

        editText.setSelection(editText.text.length)
    }
}