package kh.edu.rupp.ite.mad_project_y4_s1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set drawables programmatically
        findViewById<ImageButton>(R.id.searchButton).setImageResource(R.drawable.ic_search)
        findViewById<ImageButton>(R.id.shoppingButton).setImageResource(R.drawable.ic_shopping_bag)
        findViewById<ImageView>(R.id.logoImage).setImageResource(R.drawable.app_logo)

        val coverImageCarousel: ViewPager2 = findViewById(R.id.coverImageCarousel)
        val images = listOf(
            R.drawable.cover_image1,
            R.drawable.cover_image2,
            R.drawable.cover_image3
        )
        
        coverImageCarousel.adapter = CoverImageAdapter(images)
    }
}