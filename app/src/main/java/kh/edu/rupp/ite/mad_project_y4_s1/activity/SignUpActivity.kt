package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.AuthViewModel

class SignUpActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        val emailEditText: EditText = findViewById(R.id.editEmail)
        val passwordEditText: EditText = findViewById(R.id.editPassword)
        val confirmPasswordEditText: EditText = findViewById(R.id.editConfirmPassword)
        val signUpButton: Button = findViewById(R.id.signUpButton)
        val loginText: TextView = findViewById(R.id.textLetter)

        lifecycleScope.launch {
            viewModel.authState.collect { result ->
                result?.let {
                    if (it.isSuccess) {
                        Toast.makeText(baseContext, "Sign up successful.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(baseContext, it.error ?: "Sign up failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    viewModel.signUp(email, password)
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        loginText.setOnClickListener {
            finish()
        }
    }
}
