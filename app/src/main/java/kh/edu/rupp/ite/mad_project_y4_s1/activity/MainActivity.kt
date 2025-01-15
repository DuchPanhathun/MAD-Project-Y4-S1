package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import android.view.LayoutInflater
import android.content.Context
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.CoverImageAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.lifecycle.ViewModelProvider
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.FavoritesViewModel
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.BannerViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var coverImageCarousel: ViewPager2
    private val sliderHandler = Handler(Looper.getMainLooper())
    private val sliderRunnable = Runnable { 
        val itemCount = coverImageCarousel.adapter?.itemCount ?: 0
        if (itemCount > 0) {
            coverImageCarousel.currentItem = (coverImageCarousel.currentItem + 1) % itemCount
        }
    }
    private lateinit var customMenuView: View
    private lateinit var tabIndicator: View
    private lateinit var menuItemsRecyclerView: RecyclerView
    private lateinit var popupWindow: PopupWindow
    private lateinit var auth: FirebaseAuth
    private lateinit var loginLogoutButton: TextView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var bannerViewModel: BannerViewModel
    private lateinit var coverImageAdapter: CoverImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_main)

        // Initialize banner carousel
        setupBannerCarousel()

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

        // Set up auto-sliding
        coverImageCarousel.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (coverImageAdapter.itemCount > 0) {
                    sliderHandler.removeCallbacks(sliderRunnable)
                    sliderHandler.postDelayed(sliderRunnable, 2000)
                }
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

    private fun setupBannerCarousel() {
        coverImageCarousel = findViewById(R.id.coverImageCarousel)
        coverImageAdapter = CoverImageAdapter()
        coverImageCarousel.adapter = coverImageAdapter

        // Initialize ViewModel
        bannerViewModel = ViewModelProvider(this)[BannerViewModel::class.java]

        // Observe banner changes
        bannerViewModel.banners.observe(this) { banners ->
            coverImageAdapter.updateBanners(banners)
            // Only start auto-sliding if we have banners
            if (banners.isNotEmpty()) {
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 2000)
            }
        }

        // Set up the indicator
        val tabLayout: TabLayout = findViewById(R.id.indicator)
        TabLayoutMediator(tabLayout, coverImageCarousel) { _, _ -> }.attach()


        // Set up auto-sliding
        coverImageCarousel.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Only continue auto-sliding if we have items
                if (coverImageAdapter.itemCount > 0) {
                    sliderHandler.removeCallbacks(sliderRunnable)
                    sliderHandler.postDelayed(sliderRunnable, 2000)
                }
            }
        })

        // Start fetching banners
        bannerViewModel.fetchBanners()
    }


    // Add this extension function to convert dp to pixels
    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
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