package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kh.edu.rupp.ite.mad_project_y4_s1.R

class CategoryListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_listview)

        val productItemWrapper = findViewById<FrameLayout>(R.id.productItemWrapper)
        val heartIconWrapper = findViewById<RelativeLayout>(R.id.heartIconWrapper)

        productItemWrapper.setOnClickListener {
            // Navigate to product detail page
            val intent = Intent(this, ProductDetailActivity::class.java)
            startActivity(intent)
        }

        heartIconWrapper.setOnClickListener {
            // Handle heart icon click (e.g., add to favorites)
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
        }
    }
}