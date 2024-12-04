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
    
    private var purchasedItems: List<PurchasedItem> = emptyList()

    fun updatePurchased(newPurchased: List<PurchasedItem>) {
        Log.d("PurchasedAdapter", "Updating purchased items. New size: ${newPurchased.size}")
        purchasedItems = newPurchased
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchasedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_basket, parent, false)
        return PurchasedViewHolder(view)
    }

    override fun onBindViewHolder(holder: PurchasedViewHolder, position: Int) {
        Log.d("PurchasedAdapter", "Binding item at position $position")
        holder.bind(purchasedItems[position])
    }

    override fun getItemCount(): Int = purchasedItems.size

    class PurchasedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.purchasedImageView)
        private val brandNameTextView: TextView = itemView.findViewById(R.id.purchasedBrandNameTextView)
        private val typeTextView: TextView = itemView.findViewById(R.id.purchasedTypeTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.purchasedPriceTextView)
        private val sizesTextView: TextView = itemView.findViewById(R.id.purchasedSizesTextView)
        private val purchaseDateTextView: TextView = itemView.findViewById(R.id.purchaseDateTextView)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        private val decreaseButton: ImageButton = itemView.findViewById(R.id.decreaseQuantityButton)
        private val increaseButton: ImageButton = itemView.findViewById(R.id.increaseQuantityButton)

        fun bind(item: PurchasedItem) {
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .into(imageView)
            
            brandNameTextView.text = item.brandName
            typeTextView.text = item.type
            priceTextView.text = "$${item.price}"
            sizesTextView.text = "Size: ${item.selectedSize}, Color: ${item.selectedColor}"
            
            // Format the purchase date
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val purchaseDate = Date(item.purchaseDate)
            purchaseDateTextView.text = "Purchased on: ${dateFormat.format(purchaseDate)}"
            
            quantityTextView.text = item.quantity.toString()
            
            decreaseButton.setOnClickListener {
                if (item.quantity > 1) {
                    item.quantity--
                    quantityTextView.text = item.quantity.toString()
                    updateQuantityInFirestore(item)
                }
            }
            
            increaseButton.setOnClickListener {
                item.quantity++
                quantityTextView.text = item.quantity.toString()
                updateQuantityInFirestore(item)
            }
        }

        private fun updateQuantityInFirestore(item: PurchasedItem) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("purchased")
                .document(item.id)
                .update("quantity", item.quantity)
                .addOnFailureListener { e ->
                    Log.e("PurchasedAdapter", "Error updating quantity", e)
                }
        }
    }
} 