package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.ImageSliderAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.ColorAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.SizeAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.CareDetailsAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.model.Item
import android.widget.ImageButton
import androidx.activity.viewModels
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.FavoritesViewModel
import kh.edu.rupp.ite.mad_project_y4_s1.model.FavoriteItem
import android.widget.Toast
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import android.content.Intent
import kh.edu.rupp.ite.mad_project_y4_s1.activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ItemDetailActivity : AppCompatActivity() {
    private lateinit var imageViewPager: ViewPager2
    private lateinit var brandNameTextView: TextView
    private lateinit var typeTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var colorsRecyclerView: RecyclerView
    private lateinit var sizesRecyclerView: RecyclerView
    private lateinit var materialDetailTextView: TextView
    private lateinit var additionalCareDetailsTextView: TextView
    private lateinit var careDetailsRecyclerView: RecyclerView
    private lateinit var deliveryDatesTextView: TextView
    private lateinit var heartButton: ImageButton
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        auth = FirebaseAuth.getInstance()
        
        initializeViews()

        // Get item from intent
        val item = intent.getParcelableExtra<Item>("item")
        item?.let { 
            displayItemDetails(it)
            setupHeartButton(it)
        }
    }

    private fun initializeViews() {
        imageViewPager = findViewById(R.id.imageViewPager)
        brandNameTextView = findViewById(R.id.brandNameTextView)
        typeTextView = findViewById(R.id.typeTextView)
        priceTextView = findViewById(R.id.priceTextView)
        colorsRecyclerView = findViewById(R.id.colorsRecyclerView)
        sizesRecyclerView = findViewById(R.id.sizesRecyclerView)
        materialDetailTextView = findViewById(R.id.materialDetailTextView)
        additionalCareDetailsTextView = findViewById(R.id.additionalCareDetailsTextView)
        careDetailsRecyclerView = findViewById(R.id.careDetailsRecyclerView)
        deliveryDatesTextView = findViewById(R.id.deliveryDatesTextView)
        heartButton = findViewById(R.id.favoriteButton)
    }

    private fun displayItemDetails(item: Item) {
        // Set up image slider
        imageViewPager.adapter = ImageSliderAdapter(item.images)

        // Set text views
        brandNameTextView.text = item.brandName
        typeTextView.text = item.type
        priceTextView.text = "$${item.price}"
        materialDetailTextView.text = "Material: ${item.materialDetail}"
        additionalCareDetailsTextView.text = "Care: ${item.additionalCareDetails}"
        deliveryDatesTextView.text = "Delivery: ${item.deliveryStartDate} - ${item.deliveryEndDate}"

        // Set up colors RecyclerView
        colorsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ColorAdapter(item.colors)
        }

        // Set up sizes RecyclerView
        sizesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = SizeAdapter(item.sizes)
        }

        // Set up care details RecyclerView
        careDetailsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CareDetailsAdapter(item.careDetails)
        }

    }

    private fun setupHeartButton(item: Item) {
        heartButton.setOnClickListener {
            addToFavorites(item)
        }
    }

    private fun addToFavorites(item: Item) {
        if (auth.currentUser == null) {
            Toast.makeText(this, "Please login to add favorites", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            return
        }

        val favoriteItem = FavoriteItem(
            imageUrl = item.images.firstOrNull() ?: "",
            brandName = item.brandName,
            type = item.type,
            price = item.price,
            sizes = item.sizes
        )
        
        viewModel.addFavorite(favoriteItem)
        Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
    }

}