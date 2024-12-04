package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.OrderPagerAdapter

class OrderActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
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

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        // Set up the BottomNavigationView listener
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Navigate to HomeActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.shoppingButton -> {
                    // Navigate to ShopActivity
                    val intent = Intent(this, ItemsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.order -> {
                    startActivity(Intent(this, OrderActivity::class.java))
                    true
                }
                R.id.nav_blog -> {
                    // Navigate to BlogActivity
                    val intent = Intent(this, BlogActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    // Navigate to ProfileActivity
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }

        }

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        // Set up ViewPager with fragments
        val adapter = OrderPagerAdapter(this)
        viewPager.adapter = adapter

        // Connect TabLayout with ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Basket"
                1 -> "History Purchased"
                2 -> "Favorites"
                else -> ""
            }
        }.attach()
    }
} 