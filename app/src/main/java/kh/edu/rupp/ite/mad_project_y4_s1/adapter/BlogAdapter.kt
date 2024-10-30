package kh.edu.rupp.ite.mad_project_y4_s1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.model.BlogItem

class BlogAdapter(private val items: List<BlogItem>) : RecyclerView.Adapter<BlogAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coverImageView: ImageView = view.findViewById(R.id.blogimages)
        val titleTextView: TextView = view.findViewById(R.id.textblog)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        holder.titleTextView.text = item.title
        Glide.with(holder.itemView.context)
            .load(item.coverImage)
            .into(holder.coverImageView)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
