package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.bottomnavigation.BottomNavigationView
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.AdditionalPhotosAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.model.Blog
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.BlogViewModel

class BlogDetailActivity : AppCompatActivity() {

    private val viewModel: BlogViewModel by viewModels()
    private lateinit var popupWindow: PopupWindow
    private lateinit var customMenuView: View
    private lateinit var tabIndicator: View
    private lateinit var menuItemsRecyclerView: RecyclerView
    private lateinit var loginLogoutButton: TextView
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Declare the BottomNavigationView
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_detail)

        // Set up Bottom Navigation
        setupBottomNavigation()

        // Get blog from intent and set it in ViewModel
        intent.getParcelableExtra<Blog>("blog")?.let { blog ->
            viewModel.setSelectedBlog(blog)
        }

        // Handle the back button click
        findViewById<ImageButton>(R.id.backButton)?.setOnClickListener {
            val origin = intent.getStringExtra("origin")
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
                    finish()
                }
            }
        }

        // Add blog text click handler
        findViewById<TextView>(R.id.blogText).setOnClickListener {
            startActivity(Intent(this, BlogActivity::class.java))
        }

        // Add contact us
        val contactUsText: TextView = findViewById(R.id.contact_us_Text)
        contactUsText.setOnClickListener {
            val intent = Intent(this, ContactUsActivity::class.java)
            intent.putExtra("origin", "BlogDetailActivity") // Specify the origin
            startActivity(intent)
        }

        // Add About
        val aboutText: TextView = findViewById(R.id.aboutText)
        aboutText.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            intent.putExtra("origin", "BlogDetailActivity") // Specify the origin
            startActivity(intent)
        }

        // Observe selected blog
        viewModel.selectedBlog.observe(this) { blog ->
            findViewById<TextView>(R.id.titleText).text = blog.title
            findViewById<TextView>(R.id.detailText).text = blog.detail
            findViewById<TextView>(R.id.additionalDetailsText).text = blog.additionalDetails

            Glide.with(this)
                .load(blog.coverImage)
                .into(findViewById<ImageView>(R.id.coverImage))

            // Setup additional photos recycler view
            val photosRecyclerView = findViewById<RecyclerView>(R.id.additionalPhotosRecyclerView)
            photosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            photosRecyclerView.adapter = AdditionalPhotosAdapter(blog.additionalPhotos)
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
                    startActivity(Intent(this, ItemsActivity::class.java))
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
