package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kh.edu.rupp.ite.mad_project_y4_s1.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about)

        // Handle the back button click
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val origin = intent.getStringExtra("origin") // Retrieve the origin
            when (origin) {
                "BlogDetailActivity" -> {
                    val intent = Intent(this, BlogDetailActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
                "BlogActivity" -> {
                    val intent = Intent(this, BlogActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
                "MainActivity" -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
                else -> {
                    finish() // Default behavior if no origin is specified
                }
            }
        }
    }
    fun onSearchButtonClick(view: View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}
