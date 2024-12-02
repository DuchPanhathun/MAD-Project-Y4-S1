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
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.ItemsAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiState
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.ItemsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class ItemsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val viewModel: ItemsViewModel by viewModels()
    private lateinit var popupWindow: PopupWindow
    private lateinit var customMenuView: View
    private lateinit var tabIndicator: View
    private lateinit var menuItemsRecyclerView: RecyclerView
    private lateinit var loginLogoutButton: TextView
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

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

        recyclerView = findViewById(R.id.itemsRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        setupRecyclerView()
        observeState()

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
                "BlogGridActivity" -> {
                    val intent = Intent(this, BlogGridActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
                "MainActivity" -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Clear the stack above MainActivity
                    startActivity(intent)
                }
                else -> {
                    finish() // Default behavior if no origin is specified
                }
            }
        }

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
        // Set up exit button
        val exitButton: ImageButton = customMenuView.findViewById(R.id.exitButton)
        exitButton.setOnClickListener {
            popupWindow.dismiss()
        }

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

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun truncateTitle(title: String, wordLimit: Int): String {
        val words = title.split(" ")
        return if (words.size > wordLimit) {
            words.take(wordLimit).joinToString(" ") + "..."
        } else {
            title
        }
    }




    private fun observeState() {
        viewModel.itemsState.observe(this) { response ->
            when (response.status) {
                ApiState.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                ApiState.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    response.data?.let { items ->
                        val adapter = ItemsAdapter(items) { item ->
                            val intent = Intent(this, ItemDetailActivity::class.java)
                            intent.putExtra("item", item)
                            startActivity(intent)
                        }
                        recyclerView.adapter = adapter
                    }
                }

                ApiState.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, response.error ?: "Error loading items", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
