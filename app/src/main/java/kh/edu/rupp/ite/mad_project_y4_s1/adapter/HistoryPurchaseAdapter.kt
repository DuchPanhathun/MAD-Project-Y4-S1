package kh.edu.rupp.ite.mad_project_y4_s1.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.model.HistoryPurchaseItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryPurchaseAdapter : RecyclerView.Adapter<HistoryPurchaseAdapter.HistoryViewHolder>() {
    private var historyItems: List<HistoryPurchaseItem> = emptyList()

    fun updateHistory(newHistory: List<HistoryPurchaseItem>) {
        historyItems = newHistory
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_purchase, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyItems[position]

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.imageView)

        holder.brandNameTextView.text = item.brandName
        holder.typeTextView.text = item.type
        
        // Individual item price (before quantity)
        holder.priceTextView.text = "Unit Price: $${item.price}"
        holder.detailsTextView.text = "Size: ${item.selectedSize}, Color: ${item.selectedColor}"
        
        val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        holder.dateTextView.text = "Purchased on: ${dateFormat.format(Date(item.purchaseDate))}"

        // Handle price display with discounts
        if (item.discountPercentage > 0) {
            // Calculate prices
            val unitOriginalPrice = item.originalPrice
            val unitFinalPrice = item.finalPrice
            val totalOriginalPrice = unitOriginalPrice * item.quantity
            val totalFinalPrice = unitFinalPrice * item.quantity

            // Show original price with strikethrough
            holder.originalPriceText.apply {
                visibility = View.VISIBLE
                text = "$${String.format("%.2f", unitOriginalPrice)}"
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            
            // Show discount info
            holder.discountText.apply {
                visibility = View.VISIBLE
                text = "-${String.format("%.0f", item.discountPercentage * 100)}%"
            }
            
            // Show final unit price
            holder.finalPriceText.text = "$${String.format("%.2f", unitFinalPrice)}"

            // Show quantity and total with discount
            holder.totalTextView.text = "Quantity: ${item.quantity} | Total: $${String.format("%.2f", totalFinalPrice)}"
        } else {
            // No discount case
            val unitPrice = item.finalPrice
            val totalPrice = unitPrice * item.quantity

            // Hide discount-related views
            holder.originalPriceText.visibility = View.GONE
            holder.discountText.visibility = View.GONE
            
            // Show regular price
            holder.finalPriceText.text = "$${String.format("%.2f", unitPrice)}"
            
            // Show quantity and total
            holder.totalTextView.text = "Quantity: ${item.quantity} | Total: $${String.format("%.2f", totalPrice)}"
        }
    }

    override fun getItemCount(): Int = historyItems.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.historyImageView)
        val brandNameTextView: TextView = itemView.findViewById(R.id.historyBrandNameTextView)
        val typeTextView: TextView = itemView.findViewById(R.id.historyTypeTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.historyPriceTextView)
        val detailsTextView: TextView = itemView.findViewById(R.id.historyDetailsTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.historyDateTextView)
        val totalTextView: TextView = itemView.findViewById(R.id.historyTotalTextView)
        val originalPriceText: TextView = itemView.findViewById(R.id.originalPriceText)
        val discountText: TextView = itemView.findViewById(R.id.discountText)
        val finalPriceText: TextView = itemView.findViewById(R.id.finalPriceText)
    }
} 