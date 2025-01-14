package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
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
import com.google.firebase.auth.FirebaseAuth
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
    private lateinit var forwardImage1: ImageView
    private lateinit var forwardImage2: ImageView
    private lateinit var forwardImage3: ImageView
    private lateinit var estimatedDeliveryLabel1: TextView
    private lateinit var estimatedDeliveryLabel2: TextView
    private lateinit var estimatedDeliveryLabel3: TextView
    private lateinit var deliveryDate1: TextView
    private lateinit var deliveryDate2: TextView
    private lateinit var deliveryDate3: TextView
    private var isForward1Down = true
    private var isForward2Down = true
    private var isForward3Down = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        initializeViews()
        setupBackButton()

        auth = FirebaseAuth.getInstance()

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
        forwardImage1 = findViewById(R.id.forward_image1)
        forwardImage2 = findViewById(R.id.forward_image2)
        forwardImage3 = findViewById(R.id.forward_image3)
        estimatedDeliveryLabel1 = findViewById(R.id.estimated_delivery_label1)
        estimatedDeliveryLabel2 = findViewById(R.id.estimated_delivery_label2)
        estimatedDeliveryLabel3 = findViewById(R.id.estimated_delivery_label3)
        deliveryDate1 = findViewById(R.id.delivery_date1)
        deliveryDate2 = findViewById(R.id.delivery_date2)
        deliveryDate3 = findViewById(R.id.delivery_date3)


        estimatedDeliveryLabel1.visibility = View.GONE
        estimatedDeliveryLabel2.visibility = View.GONE
        estimatedDeliveryLabel3.visibility = View.GONE
        deliveryDate1.visibility = View.GONE
        deliveryDate2.visibility = View.GONE
        deliveryDate3.visibility = View.GONE

        // Reset arrow state
        isForward1Down = false
        isForward2Down = false
        isForward3Down = false
        forwardImage1.setImageResource(R.drawable.drop_down) // Replace with your forward arrow drawable
        forwardImage2.setImageResource(R.drawable.drop_down) // Replace with your forward arrow drawable
        forwardImage3.setImageResource(R.drawable.drop_down)

        forwardImage1.setOnClickListener {
            toggleDeliveryDetails(1)
        }
        forwardImage2.setOnClickListener {
            toggleDeliveryDetails(2)
        }
        forwardImage3.setOnClickListener {
            toggleDeliveryDetails(3)
        }


    }

    private fun toggleDeliveryDetails(index: Int) {
        when (index) {
            1 -> {
                val isExpanded = toggleVisibility(
                    estimatedDeliveryLabel1,
                    deliveryDate1,
                    isForward1Down
                )
                isForward1Down = isExpanded
                toggleArrow(forwardImage1, isExpanded)
            }
            2 -> {
                val isExpanded = toggleVisibility(
                    estimatedDeliveryLabel2,
                    deliveryDate2,
                    isForward2Down
                )
                isForward2Down = isExpanded
                toggleArrow(forwardImage2, isExpanded)
            }
            3 -> {
                val isExpanded = toggleVisibility(
                    estimatedDeliveryLabel3,
                    deliveryDate3,
                    isForward3Down
                )
                isForward3Down = isExpanded
                toggleArrow(forwardImage3, isExpanded)
            }
        }
    }

    private fun toggleVisibility(label: TextView, date: TextView, isDown: Boolean): Boolean {
        if (isDown) {
            label.visibility = View.GONE
            date.visibility = View.GONE
        } else {
            label.visibility = View.VISIBLE
            date.visibility = View.VISIBLE
        }
        return !isDown
    }

    private fun toggleArrow(imageView: ImageView, isDown: Boolean) {
        if (isDown) {
            imageView.setImageResource(R.drawable.forward_) // Replace with your downward arrow drawable
        } else {
            imageView.setImageResource(R.drawable.drop_down) // Replace with your forward arrow drawable
        }
    }
    private fun setupBackButton() {
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val origin = intent.getStringExtra("origin") // Retrieve the origin
            val intent = when (origin) {
                "BlogDetailActivity" -> Intent(this, BlogDetailActivity::class.java)
                "BlogActivity" -> Intent(this, BlogActivity::class.java)
                "MainActivity" -> Intent(this, MainActivity::class.java)
                else -> null
            }
            intent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent?.let { startActivity(it) } ?: finish()
        }
    }

    private fun displayItemDetails(item: Item) {
        imageViewPager.adapter = ImageSliderAdapter(item.images)
        brandNameTextView.text = item.brandName
        typeTextView.text = item.type
        priceTextView.text = "$${item.price}"
        materialDetailTextView.text = "Material: ${item.materialDetail}"
        additionalCareDetailsTextView.text = "Care: ${item.additionalCareDetails}"
        deliveryDatesTextView.text = "Delivery: ${item.deliveryStartDate} - ${item.deliveryEndDate}"

        setupRecyclerView(colorsRecyclerView, item.colors, ColorAdapter(item.colors)) { color ->
            selectedColor = color
            updatePurchaseButtonState()
        }

        setupRecyclerView(sizesRecyclerView, item.sizes, SizeAdapter(item.sizes)) { size ->
            selectedSize = size
            updatePurchaseButtonState()
        }

        careDetailsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CareDetailsAdapter(item.careDetails)
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, items: List<String>, adapter: RecyclerView.Adapter<*>, onItemSelected: (String) -> Unit) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }
        (adapter as? ColorAdapter)?.setOnColorSelectedListener(onItemSelected)
        (adapter as? SizeAdapter)?.setOnSizeSelectedListener(onItemSelected)
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
    fun onSearchButtonClick(view: View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

}
