package kh.edu.rupp.ite.mad_project_y4_s1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private lateinit var coverImageCarousel: ViewPager2
    private val sliderHandler = Handler(Looper.getMainLooper())
    private val sliderRunnable = Runnable { 
        coverImageCarousel.currentItem = (coverImageCarousel.currentItem + 1) % (coverImageCarousel.adapter?.itemCount ?: 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set drawables programmatically
        findViewById<ImageButton>(R.id.searchButton).setImageResource(R.drawable.ic_search)
        findViewById<ImageButton>(R.id.shoppingButton).setImageResource(R.drawable.ic_shopping_bag)

        coverImageCarousel = findViewById(R.id.coverImageCarousel)
        val images = listOf(
            R.drawable.cover_image1,
            R.drawable.cover_image2,
            R.drawable.cover_image3
        )
        
        coverImageCarousel.adapter = CoverImageAdapter(images)

        // Set up the indicator
        val tabLayout: TabLayout = findViewById(R.id.indicator)
        TabLayoutMediator(tabLayout, coverImageCarousel) { _, _ -> }.attach()

        // Set up auto-sliding
        coverImageCarousel.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 2000) // Change image every 2 seconds
            }
        })
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 2000)
    }
}