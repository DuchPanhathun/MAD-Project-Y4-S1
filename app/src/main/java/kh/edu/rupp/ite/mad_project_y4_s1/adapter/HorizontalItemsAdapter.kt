package kh.edu.rupp.ite.mad_project_y4_s1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.model.Item

class HorizontalItemsAdapter(
    private var items: List<Item>,
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<HorizontalItemsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.itemImage)
        val brandNameText: TextView = view.findViewById(R.id.brandNameText)
        val priceText: TextView = view.findViewById(R.id.priceText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horizontal_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Load the first image
        if (item.images.isNotEmpty()) {
            Glide.with(holder.itemImage.context)
                .load(item.images[0])
                .into(holder.itemImage)
        }

        // Set text with truncation
        holder.brandNameText.text = truncateTitle(item.brandName, 20)
        holder.priceText.text = "$${item.price}"

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount() = items.size

    private fun truncateTitle(title: String, charLimit: Int): String {
        return if (title.length > charLimit) {
            title.take(charLimit) + "..."
        } else {
            title
        }
    }

    fun submitList(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }
}