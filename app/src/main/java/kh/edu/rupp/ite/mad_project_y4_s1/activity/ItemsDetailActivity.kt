package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
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
import androidx.activity.viewModels
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.FavoritesViewModel
import kh.edu.rupp.ite.mad_project_y4_s1.model.FavoriteItem
import android.widget.Toast
<<<<<<< HEAD
=======
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import kh.edu.rupp.ite.mad_project_y4_s1.activity.LoginActivity
>>>>>>> origin/thun
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Button
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.PurchasedViewModel
import kh.edu.rupp.ite.mad_project_y4_s1.model.PurchasedItem

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
    private lateinit var purchaseButton: ImageButton
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private val purchasedViewModel: PurchasedViewModel by viewModels()
    private var selectedSize: String? = null
    private var selectedColor: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
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

        auth = FirebaseAuth.getInstance()
        
        initializeViews()

        // Get item from intent
        val item = intent.getParcelableExtra<Item>("item")
        item?.let { 
            displayItemDetails(it)
            setupHeartButton(it)
            setupPurchaseButton(it)
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
        purchaseButton = findViewById(R.id.purchaseButton)
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

        // Set up colors RecyclerView with selection
        colorsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val colorAdapter = ColorAdapter(item.colors)
            adapter = colorAdapter
            
            colorAdapter.setOnColorSelectedListener { color ->
                selectedColor = color
                updatePurchaseButtonState()
            }
        }

        // Set up sizes RecyclerView with selection
        sizesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val sizeAdapter = SizeAdapter(item.sizes)
            adapter = sizeAdapter
            
            sizeAdapter.setOnSizeSelectedListener { size ->
                selectedSize = size
                updatePurchaseButtonState()
            }
        }

        // Set up care details RecyclerView
        careDetailsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CareDetailsAdapter(item.careDetails)
        }

    }

    private fun updatePurchaseButtonState() {
        purchaseButton.isEnabled = selectedSize != null && selectedColor != null
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

    private fun setupPurchaseButton(item: Item) {
        purchaseButton.setOnClickListener {
            purchaseItem(item)
        }
    }

    private fun purchaseItem(item: Item) {
        if (auth.currentUser == null) {
            Toast.makeText(this, "Please login to purchase items", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            return
        }

        if (selectedSize == null || selectedColor == null) {
            Toast.makeText(this, "Please select both size and color", Toast.LENGTH_SHORT).show()
            return
        }

        val purchasedItem = PurchasedItem(
            imageUrl = item.images.firstOrNull() ?: "",
            brandName = item.brandName,
            type = item.type,
            price = item.price,
            selectedSize = selectedSize!!,
            selectedColor = selectedColor!!,
            purchaseDate = System.currentTimeMillis()
        )
        
        purchasedViewModel.addPurchase(purchasedItem)
        Toast.makeText(this, "Item purchased successfully", Toast.LENGTH_SHORT).show()
    }

}