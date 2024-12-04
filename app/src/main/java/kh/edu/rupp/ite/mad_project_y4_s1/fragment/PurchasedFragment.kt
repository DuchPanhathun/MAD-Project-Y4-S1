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

class PurchasedFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateText: TextView
    private lateinit var totalItemsTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var totalSection: View
    private lateinit var purchaseButton: Button
    private val purchasedAdapter = PurchasedAdapter()
    private val viewModel: PurchasedViewModel by activityViewModels()

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

        setupRecyclerView()
        observePurchased()

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
        totalPriceTextView.text = "Total Price: $${String.format("%.2f", totalPrice)}"
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
                        val historyItem = HistoryPurchaseItem(
                            imageUrl = item.imageUrl,
                            brandName = item.brandName,
                            type = item.type,
                            price = item.price,
                            selectedSize = item.selectedSize,
                            selectedColor = item.selectedColor,
                            purchaseDate = System.currentTimeMillis(),
                            quantity = item.quantity,
                            totalPrice = item.price.removePrefix("$").toDouble() * item.quantity
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
} 