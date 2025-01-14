package kh.edu.rupp.ite.mad_project_y4_s1.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.model.FavoriteItem

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {
    
    private var favorites: List<FavoriteItem> = emptyList()

    // Declare a variable to hold the listener function
    private var onItemClick: ((FavoriteItem) -> Unit)? = null
    // This function allows the caller to set a custom listener function to handle item clicks.
    fun setOnItemClick(listener: (FavoriteItem) -> Unit) {
        onItemClick = listener
    }

    fun updateFavorites(newFavorites: List<FavoriteItem>) {
        Log.d("FavoritesAdapter", "Updating favorites. New size: ${newFavorites.size}")
        Log.d("FavoritesAdapter", "New favorites content: $newFavorites")
        favorites = newFavorites
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        Log.d("FavoritesAdapter", "Binding item at position $position")
        val item = favorites[position]
        holder.bind(item)
        // Add a click listener to the item view to handle item clicks
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = favorites.size

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.favoriteImageView)
        private val brandNameTextView: TextView = itemView.findViewById(R.id.favoriteBrandNameTextView)
        private val typeTextView: TextView = itemView.findViewById(R.id.favoriteTypeTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.favoritePriceTextView)
        private val sizesTextView: TextView = itemView.findViewById(R.id.favoriteSizesTextView)

        fun bind(item: FavoriteItem) {
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .into(imageView)
            
            brandNameTextView.text = item.brandName
            typeTextView.text = item.type
            priceTextView.text = "$${item.price}"
            sizesTextView.text = "Sizes: ${item.sizes.joinToString(", ")}"
        }
    }
} 