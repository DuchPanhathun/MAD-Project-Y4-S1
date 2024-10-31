package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kh.edu.rupp.ite.mad_project_y4_s1.R

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)

        auth = Firebase.auth

        val emailEditText: EditText = findViewById(R.id.editEmail)
        val passwordEditText: EditText = findViewById(R.id.editPassword)
        val loginButton: Button = findViewById(R.id.loginButton)
        val signUpText: TextView = findViewById(R.id.textLetter)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, "Login successful.", Toast.LENGTH_SHORT).show()
                            finish() // This will return to the MainActivity
                        } else {
                            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        signUpText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
