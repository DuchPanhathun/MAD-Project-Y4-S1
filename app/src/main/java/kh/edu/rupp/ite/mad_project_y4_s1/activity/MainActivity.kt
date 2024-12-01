package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayout
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import android.view.LayoutInflater
import android.content.Context
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.CoverImageAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.R
import androidx.appcompat.widget.SearchView
import android.widget.EditText

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
    private lateinit var searchView: SearchView
    private lateinit var searchButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
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

        // Add menu functionality
        val menuButton: ImageButton = findViewById(R.id.menuButton)
        menuButton.setOnClickListener { 
            showCustomMenu()
        }

        // Add this new code to handle the click event for the shopping button
        val shoppingButton: ImageButton = findViewById(R.id.shoppingButton)
        shoppingButton.setOnClickListener {
            val intent = Intent(this, ItemsActivity::class.java)
            startActivity(intent)
        }

        // Add this new code to handle the click event
        val newArrivalText: TextView = findViewById(R.id.newArrivalText)
        newArrivalText.setOnClickListener {
            Log.d("MainActivity", "New Arrival text clicked")
            try {
                val intent = Intent(this, BlogGridActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error starting BlogGridActivity", e)
                Toast.makeText(this, "Error opening blog grid", Toast.LENGTH_SHORT).show()
            }
        }

        val exploreMoreLayout: LinearLayout = findViewById(R.id.exploreMoreLayout)
        exploreMoreLayout.setOnClickListener {
            Log.d("MainActivity", "Explore More clicked")
            try {
                val intent = Intent(this, BlogGridActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error starting BlogGridActivity", e)
                Toast.makeText(this, "Error opening blog grid", Toast.LENGTH_SHORT).show()
            }
        }

        // Add blog text click handler
        findViewById<TextView>(R.id.blogText).setOnClickListener {
            startActivity(Intent(this, BlogActivity::class.java))
        }

        setupSearch()
    }

    private fun setupSearch() {
        searchView = findViewById(R.id.searchView)
        searchButton = findViewById(R.id.searchButton)

        // Make SearchView full width and customize its appearance
        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(Color.BLACK)
        searchEditText.setHintTextColor(Color.GRAY)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { performSearch(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { performSearch(it) }
                return true
            }
        })
    }

    fun onSearchButtonClick(view: View) {
        if (searchView.visibility == View.GONE) {
            searchView.visibility = View.VISIBLE
            searchView.isIconified = false  // Shows keyboard and expands SearchView
        } else {
            searchView.visibility = View.GONE
            searchView.setQuery("", false)  // Clears the query
            searchView.isIconified = true
        }
    }

    private fun performSearch(query: String) {
        // TODO: Implement your search logic here
        // For example:
        Toast.makeText(this, "Searching for: $query", Toast.LENGTH_SHORT).show()
        
        // You might want to:
        // 1. Start a new SearchResultsActivity with the query
        // 2. Filter your existing data
        // 3. Make an API call to search products
    }

    private fun showCustomMenu() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        customMenuView = inflater.inflate(R.layout.custom_menu_layout, null)

        popupWindow = PopupWindow(
            customMenuView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        // Set up tabs
        val settingTab: TextView = customMenuView.findViewById(R.id.settingTab)
//        val menTab: TextView = customMenuView.findViewById(R.id.menTab)
//        val womenTab: TextView = customMenuView.findViewById(R.id.womenTab)
        tabIndicator = customMenuView.findViewById(R.id.tabIndicator)
        menuItemsRecyclerView = customMenuView.findViewById(R.id.menuItemsRecyclerView)

        settingTab.setOnClickListener { selectTab(it, R.id.setting_group) }
//        menTab.setOnClickListener { selectTab(it, R.id.men_group) }
//        womenTab.setOnClickListener { selectTab(it, R.id.women_group) }

        // Set up login/logout button
        loginLogoutButton = customMenuView.findViewById(R.id.loginLogoutButton)
        updateLoginLogoutButton()

        // Show the popup window
        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0)

        // Initially select the setting tab
        selectTab(settingTab, R.id.setting_group)
    }

    private fun setupTabs() {
        val settingTab: TextView = customMenuView.findViewById(R.id.settingTab)
//        val menTab: TextView = customMenuView.findViewById(R.id.menTab)
//        val womenTab: TextView = customMenuView.findViewById(R.id.womenTab)

        settingTab.setOnClickListener { selectTab(it, R.id.setting_group) }
//        menTab.setOnClickListener { selectTab(it, R.id.men_group) }
//        womenTab.setOnClickListener { selectTab(it, R.id.women_group) }

        selectTab(settingTab, R.id.setting_group)
    }

    private fun selectTab(view: View, menuGroupId: Int) {
        val tabLayout = customMenuView.findViewById<LinearLayout>(R.id.tabLayout)
        val tabPosition = tabLayout.indexOfChild(view)
        val tabWidth = view.width
        val indicatorWidth = tabWidth / 3

        val params = tabIndicator.layoutParams as LinearLayout.LayoutParams
        params.width = indicatorWidth
        params.leftMargin = (tabPosition * tabWidth) + (tabWidth - indicatorWidth) / 2
        tabIndicator.layoutParams = params

        setupMenuItems(menuGroupId)
    }

    private fun setupMenuItems(menuGroupId: Int) {
        val menu = PopupMenu(this, null).menu
        menuInflater.inflate(R.menu.main_menu, menu)
        val items = mutableListOf<MenuItem>()
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item.groupId == menuGroupId) {
                items.add(item)
            }
        }

        menuItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        menuItemsRecyclerView.adapter = MenuItemAdapter(items)
    }

    private inner class MenuItemAdapter(private val items: List<MenuItem>) :
        RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {

        inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(R.id.menu_item_text)
            val iconView: ImageView = view.findViewById(R.id.menu_item_icon)

            init {
                view.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = items[position]
                        when (item.itemId) {
                            R.id.menu_profile -> {
                                if (auth.currentUser != null) {
                                    startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                                } else {
                                    Toast.makeText(this@MainActivity, 
                                        "Please login first", 
                                        Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                                }
                                popupWindow.dismiss()
                            }
                            // Handle other menu items here
                        }
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.menu_item_layout, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.textView.text = item.title
            if (item.groupId == R.id.setting_group) {
                holder.iconView.visibility = View.VISIBLE
            } else {
                holder.iconView.visibility = View.GONE
            }
        }

        override fun getItemCount() = items.size
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
}