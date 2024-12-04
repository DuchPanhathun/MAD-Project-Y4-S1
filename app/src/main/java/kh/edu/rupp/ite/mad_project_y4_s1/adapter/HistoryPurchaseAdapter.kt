package kh.edu.rupp.ite.mad_project_y4_s1.adapter

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
        holder.bind(historyItems[position])
    }

    override fun getItemCount(): Int = historyItems.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.historyImageView)
        private val brandNameTextView: TextView = itemView.findViewById(R.id.historyBrandNameTextView)
        private val typeTextView: TextView = itemView.findViewById(R.id.historyTypeTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.historyPriceTextView)
        private val detailsTextView: TextView = itemView.findViewById(R.id.historyDetailsTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.historyDateTextView)
        private val totalTextView: TextView = itemView.findViewById(R.id.historyTotalTextView)

        fun bind(item: HistoryPurchaseItem) {
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .into(imageView)

            brandNameTextView.text = item.brandName
            typeTextView.text = item.type
            priceTextView.text = "Price: $${item.price}"
            detailsTextView.text = "Size: ${item.selectedSize}, Color: ${item.selectedColor}"
            
            val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
            dateTextView.text = "Purchased on: ${dateFormat.format(Date(item.purchaseDate))}"
            
            totalTextView.text = "Quantity: ${item.quantity} | Total: $${String.format("%.2f", item.totalPrice)}"
        }
    }
} 