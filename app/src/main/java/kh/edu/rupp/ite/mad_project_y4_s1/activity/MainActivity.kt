package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.util.Log
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import android.content.Context
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.CoverImageAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.lifecycle.ViewModelProvider
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.FavoritesViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var coverImageCarousel: ViewPager2
    private val sliderHandler = Handler(Looper.getMainLooper())
    private val sliderRunnable = Runnable { 
        coverImageCarousel.currentItem = (coverImageCarousel.currentItem + 1) % (coverImageCarousel.adapter?.itemCount ?: 1)
    }
    private lateinit var customMenuView: View
    private lateinit var tabIndicator: View
    private lateinit var menuItemsRecyclerView: RecyclerView
    private lateinit var popupWindow: PopupWindow
    private lateinit var auth: FirebaseAuth
    private lateinit var loginLogoutButton: TextView
    private lateinit var searchButton: ImageButton
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewModel: FavoritesViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_main)

        coverImageCarousel = findViewById(R.id.coverImageCarousel)
        val images = listOf(
            R.drawable.cover_image1,
            R.drawable.cover_image2,
            R.drawable.cover_image3
        )
        coverImageCarousel.adapter = CoverImageAdapter(images)
        //Add About
        val aboutText: TextView = findViewById(R.id.aboutText)
        aboutText.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        //Add contact us
        val contactUsText: TextView = findViewById(R.id.contact_us_Text)
        contactUsText.setOnClickListener {
            val intent = Intent(this, ContactUsActivity::class.java)
            startActivity(intent)
        }

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

        // Add blog text click handler
        findViewById<TextView>(R.id.blogText).setOnClickListener {
            startActivity(Intent(this, BlogActivity::class.java))
        }

        // Setup bottom navigation
        setupBottomNavigation()

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[FavoritesViewModel::class.java]

        findViewById<ImageView>(R.id.facebookButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=100026153991813&mibextid=9R9pXO"))
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.instagramButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/duch_panhathun/profilecard/?igsh=MTN4dmZ6cXkxM2EzMA=="))
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.telegramButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/nhacool"))
            startActivity(intent)
        }
        // Initialize exploreMoreText and set OnClickListener
        val exploreMoreText: TextView = findViewById(R.id.exploreMoreText)
        exploreMoreText.setOnClickListener {
            // Your action when the "Explore More" text is clicked
            val intent = Intent(this, ItemsActivity::class.java)
            startActivity(intent)
        }
    }




    // Add this extension function to convert dp to pixels
    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    private fun updateLoginLogoutButton() {
        val currentUser = auth.currentUser
        Log.d("MainActivity", "Current user: ${currentUser?.email}")
        if (currentUser != null) {
            loginLogoutButton.text = "Log Out"
            loginLogoutButton.setOnClickListener {
                auth.signOut()
                updateLoginLogoutButton()
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            }
        } else {
            loginLogoutButton.text = "Log In"
            loginLogoutButton.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 2000)
        if (::popupWindow.isInitialized && popupWindow.isShowing) {
            updateLoginLogoutButton()
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        
        // Set icons programmatically if needed
        bottomNavigationView.menu.findItem(R.id.shoppingButton)?.setIcon(R.drawable.ic_shopping_bag)
        
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Keep current main content
                    true
                }
                R.id.shoppingButton -> {
                    startActivity(Intent(this, ItemsActivity::class.java))
                    true
                }
                R.id.order -> {
                    startActivity(Intent(this, OrderActivity::class.java))
                    true
                }
                R.id.nav_blog -> {
                    startActivity(Intent(this, BlogActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    if (auth.currentUser != null) {
                        startActivity(Intent(this, ProfileActivity::class.java))
                    } else {
                        Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    true
                }
                else -> false
            }
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.nav_home
    }


    fun onSearchButtonClick(view: View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

}