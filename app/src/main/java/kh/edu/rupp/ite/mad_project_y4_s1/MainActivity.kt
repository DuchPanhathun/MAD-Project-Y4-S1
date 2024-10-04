package kh.edu.rupp.ite.mad_project_y4_s1

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

        // Add menu functionality
        val menuButton: ImageButton = findViewById(R.id.menuButton)
        menuButton.setOnClickListener { showCustomMenu() }
    }

    private fun showCustomMenu() {
        popupWindow = PopupWindow(this)
        customMenuView = layoutInflater.inflate(R.layout.custom_menu_layout, null)
        popupWindow.contentView = customMenuView
        popupWindow.width = LinearLayout.LayoutParams.MATCH_PARENT
        popupWindow.height = LinearLayout.LayoutParams.MATCH_PARENT
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))  // Add this line

        // Set up exit button
        val exitButton: ImageButton = customMenuView.findViewById(R.id.exitButton)
        exitButton.setOnClickListener {
            popupWindow.dismiss()
        }

        tabIndicator = customMenuView.findViewById(R.id.tabIndicator)
        menuItemsRecyclerView = customMenuView.findViewById(R.id.menuItemsRecyclerView)

        setupTabs()
        setupMenuItems(R.id.setting_group)

        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0)
    }

    private fun setupTabs() {
        val settingTab: TextView = customMenuView.findViewById(R.id.settingTab)
        val menTab: TextView = customMenuView.findViewById(R.id.menTab)
        val womenTab: TextView = customMenuView.findViewById(R.id.womenTab)

        settingTab.setOnClickListener { selectTab(it, R.id.setting_group) }
        menTab.setOnClickListener { selectTab(it, R.id.men_group) }
        womenTab.setOnClickListener { selectTab(it, R.id.women_group) }

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

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 2000)
    }
}