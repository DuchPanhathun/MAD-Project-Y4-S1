package kh.edu.rupp.ite.mad_project_y4_s1.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kh.edu.rupp.ite.mad_project_y4_s1.R

class CoverImageAdapter(private val images: List<Int>) : RecyclerView.Adapter<CoverImageAdapter.CoverImageViewHolder>() {
    
    inner class CoverImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val coverImageView: ImageView = itemView.findViewById(R.id.coverImageView)

        fun bind(image: Int) {
            Log.d("CoverImageAdapter", "Binding image resource: $image")
            coverImageView.setImageResource(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoverImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cover_image, parent, false)
        return CoverImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoverImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size
}