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

class ItemsAdapter(private val items: List<Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_FOOTER = 1

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.itemImage)
        val brandName: TextView = view.findViewById(R.id.brandNameText)
        val type: TextView = view.findViewById(R.id.typeText)
        val price: TextView = view.findViewById(R.id.priceText)
        val sizes: TextView = view.findViewById(R.id.sizesText)
    }

    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) VIEW_TYPE_FOOTER else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FOOTER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.footer_layout, parent, false)
            FooterViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout, parent, false)
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val item = items[position]

            // Load the first image if available
            if (item.images.isNotEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(item.images[0])
                    .placeholder(R.drawable.placeholder_image) // Add a placeholder image
                    .error(R.drawable.error_image) // Add an error image
                    .into(holder.itemImage)
            } else {
                holder.itemImage.setImageResource(R.drawable.placeholder_image) // Add a placeholder image
            }

            holder.brandName.text = item.brandName
            holder.type.text = item.type
            holder.price.text = "$${item.price}"
            holder.sizes.text = "Available Sizes: ${item.sizes.joinToString(", ")}"
        }
        // No binding needed for FooterViewHolder
    }

    override fun getItemCount() = items.size + 1 // +1 for the footer
}