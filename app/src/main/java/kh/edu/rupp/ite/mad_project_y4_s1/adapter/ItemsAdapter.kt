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

class ItemsAdapter(
    private var items: List<Item>,
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val brandNameText: TextView = view.findViewById(R.id.brandNameText)
        val typeText: TextView = view.findViewById(R.id.typeText)
        val priceText: TextView = view.findViewById(R.id.priceText)
        val sizesText: TextView = view.findViewById(R.id.sizesText)
        val itemImage: ImageView = view.findViewById(R.id.itemImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Truncate the brand name if it exceeds 20 characters
        holder.brandNameText.text = truncateTitle(item.brandName, 20)
        holder.typeText.text = item.type
        holder.priceText.text = "$${item.price}"
        holder.sizesText.text = "Sizes: ${item.sizes.joinToString(", ")}"

        // Load the first image from the images list
        if (item.images.isNotEmpty()) {
            Glide.with(holder.itemImage.context)
                .load(item.images[0])
                .into(holder.itemImage)
        }

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount() = items.size

    // Helper function to truncate the title by characters
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
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/sal
