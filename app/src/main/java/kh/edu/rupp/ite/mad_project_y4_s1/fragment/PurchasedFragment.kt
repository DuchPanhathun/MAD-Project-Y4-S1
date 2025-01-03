package kh.edu.rupp.ite.mad_project_y4_s1.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.PurchasedAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.PurchasedViewModel
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kh.edu.rupp.ite.mad_project_y4_s1.model.HistoryPurchaseItem
import android.graphics.Paint
import com.google.android.material.textfield.TextInputEditText
import android.os.Handler
import android.os.Looper

class PurchasedFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateText: TextView
    private lateinit var totalItemsTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var totalSection: View
    private lateinit var purchaseButton: Button
    private val purchasedAdapter = PurchasedAdapter()
    private val viewModel: PurchasedViewModel by activityViewModels()
    private lateinit var promoCodeInput: TextInputEditText
    private lateinit var applyPromoButton: Button
    private lateinit var discountInfoText: TextView
    private lateinit var originalPriceTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_purchased, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        Log.d("PurchasedFragment", "Fragment created")
        
        recyclerView = view.findViewById(R.id.purchasedRecyclerView)
        emptyStateText = view.findViewById(R.id.emptyStateText)
        totalItemsTextView = view.findViewById(R.id.totalItemsTextView)
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView)
        totalSection = view.findViewById(R.id.totalSection)
        purchaseButton = view.findViewById(R.id.purchaseButton)
        promoCodeInput = view.findViewById(R.id.promoCodeInput)
        applyPromoButton = view.findViewById(R.id.applyPromoButton)
        discountInfoText = view.findViewById(R.id.discountInfoText)
        originalPriceTextView = view.findViewById(R.id.originalPriceTextView)

        setupRecyclerView()
        observePurchased()
        setupPromoCode()

        purchasedAdapter.setOnTotalPriceChangedListener { totalItems, totalPrice ->
            updateTotalSection(totalItems, totalPrice)
        }

        purchaseButton.setOnClickListener {
            handlePurchase()
        }
    }

    private fun setupRecyclerView() {
        Log.d("PurchasedFragment", "Setting up RecyclerView")
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = purchasedAdapter
        }
    }

    private fun updateTotalSection(totalItems: Int, totalPrice: Double) {
        totalItemsTextView.text = "Total Items: $totalItems"
        
        val currentItems = purchasedAdapter.getCurrentItems()
        var hasDiscount = false
        var originalTotal = 0.0
        var discountedTotal = 0.0
        
        currentItems.forEach { item ->
            val itemOriginalPrice = item.price.removePrefix("$").toDouble() * item.quantity
            originalTotal += itemOriginalPrice
            discountedTotal += item.calculateDiscountedPrice()
            if (item.discountPercentage > 0) hasDiscount = true
        }
        
        if (hasDiscount) {
            originalPriceTextView.apply {
                visibility = View.VISIBLE
                text = "Original Price: $${String.format("%.2f", originalTotal)}"
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            discountInfoText.apply {
                visibility = View.VISIBLE
                text = "Discount Applied: -$${String.format("%.2f", originalTotal - discountedTotal)}"
            }
            totalPriceTextView.text = "Final Price: $${String.format("%.2f", discountedTotal)}"
        } else {
            originalPriceTextView.visibility = View.GONE
            discountInfoText.visibility = View.GONE
            totalPriceTextView.text = "Total Price: $${String.format("%.2f", originalTotal)}"
        }
    }

    private fun observePurchased() {
        Log.d("PurchasedFragment", "Starting to observe purchased items")
        viewModel.purchased.observe(viewLifecycleOwner) { purchased ->
            Log.d("PurchasedFragment", "Received purchased update: $purchased")
            
            if (purchased.isEmpty()) {
                recyclerView.visibility = View.GONE
                emptyStateText.visibility = View.VISIBLE
                totalSection.visibility = View.GONE
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyStateText.visibility = View.GONE
                totalSection.visibility = View.VISIBLE
                purchasedAdapter.updatePurchased(purchased)
                Log.d("PurchasedFragment", "Updated adapter with purchased items")
            }
        }
    }

    private fun handlePurchase() {
        val currentItems = purchasedAdapter.getCurrentItems()
        if (currentItems.isEmpty()) {
            Toast.makeText(context, "Your basket is empty", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Purchase")
            .setMessage("Are you sure you want to complete this purchase?")
            .setPositiveButton("Yes") { _, _ ->
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val db = FirebaseFirestore.getInstance()
                    val batch = db.batch()

                    for (item in currentItems) {
                        val originalPrice = item.price.removePrefix("$").toDouble() * item.quantity
                        val finalPrice = item.calculateDiscountedPrice()
                        
                        val historyItem = HistoryPurchaseItem(
                            imageUrl = item.imageUrl,
                            brandName = item.brandName,
                            type = item.type,
                            price = item.price,
                            selectedSize = item.selectedSize,
                            selectedColor = item.selectedColor,
                            purchaseDate = System.currentTimeMillis(),
                            quantity = item.quantity,
                            originalPrice = originalPrice,
                            discountPercentage = item.discountPercentage,
                            finalPrice = finalPrice,
                            appliedPromoCode = item.appliedPromoCode
                        )

                        val historyRef = db.collection("users").document(userId)
                            .collection("purchaseHistory").document()
                        batch.set(historyRef, historyItem)

                        val basketRef = db.collection("users").document(userId)
                            .collection("purchased").document(item.id)
                        batch.delete(basketRef)
                    }

                    batch.commit()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Purchase successful!", Toast.LENGTH_SHORT).show()
                            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
                            viewPager?.currentItem = 1
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun setupPromoCode() {
        applyPromoButton.setOnClickListener {
            val promoCode = promoCodeInput.text.toString().trim()
            if (promoCode.isNotEmpty()) {
                val currentItems = purchasedAdapter.getCurrentItems()
                if (currentItems.isNotEmpty()) {
                    currentItems.forEach { item ->
                        viewModel.applyPromoCode(item.id, promoCode)
                    }
                    // Show loading indicator or disable button while processing
                    applyPromoButton.isEnabled = false
                    
                    // Re-enable button after a short delay
                    Handler(Looper.getMainLooper()).postDelayed({
                        applyPromoButton.isEnabled = true
                    }, 2000)
                } else {
                    Toast.makeText(context, "No items in cart", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please enter a promo code", Toast.LENGTH_SHORT).show()
            }
        }
    }
} 