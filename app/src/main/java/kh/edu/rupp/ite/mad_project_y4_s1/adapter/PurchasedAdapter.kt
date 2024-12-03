package kh.edu.rupp.ite.mad_project_y4_s1.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.model.PurchasedItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PurchasedAdapter : RecyclerView.Adapter<PurchasedAdapter.PurchasedViewHolder>() {
    
    private var purchasedItems: MutableList<PurchasedItem> = mutableListOf()

    fun updatePurchased(newPurchased: List<PurchasedItem>) {
        purchasedItems = newPurchased.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchasedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_purchased, parent, false)
        return PurchasedViewHolder(view)
    }

    override fun onBindViewHolder(holder: PurchasedViewHolder, position: Int) {
        holder.bind(purchasedItems[position])
    }

    override fun getItemCount(): Int = purchasedItems.size

    inner class PurchasedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.purchasedImageView)
        private val brandNameTextView: TextView = itemView.findViewById(R.id.purchasedBrandNameTextView)
        private val typeTextView: TextView = itemView.findViewById(R.id.purchasedTypeTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.purchasedPriceTextView)
        private val sizesTextView: TextView = itemView.findViewById(R.id.purchasedSizesTextView)
        private val purchaseDateTextView: TextView = itemView.findViewById(R.id.purchaseDateTextView)
        private val quantityTextView: TextView = itemView.findViewById(R.id.purchasedQuantityTextView)
        private val decreaseQuantityButton: ImageButton = itemView.findViewById(R.id.decreaseQuantity)
        private val increaseQuantityButton: ImageButton = itemView.findViewById(R.id.increaseQuantity)

        fun bind(item: PurchasedItem) {
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .into(imageView)
            
            brandNameTextView.text = item.brandName
            typeTextView.text = item.type
            sizesTextView.text = "Size: ${item.selectedSize}, Color: ${item.selectedColor}"
            
            // Calculate total price
            val unitPrice = item.price.replace("$", "").toDoubleOrNull() ?: 0.0
            val totalPrice = unitPrice * item.quantity
            
            quantityTextView.text = item.quantity.toString()
            priceTextView.text = "$${"%.2f".format(totalPrice)} (${item.quantity} × $${item.price})"
            
            // Format the purchase date
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val purchaseDate = Date(item.purchaseDate)
            purchaseDateTextView.text = "Purchased on: ${dateFormat.format(purchaseDate)}"

            // Handle quantity changes
            decreaseQuantityButton.setOnClickListener {
                if (item.quantity > 1) {
                    item.quantity--
                    updateDisplay(item, unitPrice)
                    // Update in Firestore
                    updateQuantityInFirestore(item)
                }
            }

            increaseQuantityButton.setOnClickListener {
                item.quantity++
                updateDisplay(item, unitPrice)
                // Update in Firestore
                updateQuantityInFirestore(item)
            }
        }

        private fun updateDisplay(item: PurchasedItem, unitPrice: Double) {
            quantityTextView.text = item.quantity.toString()
            priceTextView.text = "$${"%.2f".format(unitPrice * item.quantity)} (${item.quantity} × $${item.price})"
        }

        private fun updateQuantityInFirestore(item: PurchasedItem) {
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            val userId = auth.currentUser?.uid ?: return

            firestore.collection("users")
                .document(userId)
                .collection("purchased")
                .document(item.id)
                .update("quantity", item.quantity)
                .addOnSuccessListener {
                    Log.d("PurchasedAdapter", "Quantity updated successfully")
                }
                .addOnFailureListener { e ->
                    Log.w("PurchasedAdapter", "Error updating quantity", e)
                }
        }
    }
} 