package kh.edu.rupp.ite.mad_project_y4_s1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.model.Banner

class CoverImageAdapter : RecyclerView.Adapter<CoverImageAdapter.BannerViewHolder>() {
    private var banners: List<Banner> = emptyList()

    class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.coverImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cover_image, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val banner = banners[position]
        Glide.with(holder.imageView)
            .load(banner.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.imageView)
    }

    override fun getItemCount() = banners.size
<<<<<<< HEAD

=======
>>>>>>> f9bbf12
    fun updateBanners(newBanners: List<Banner>) {
        banners = newBanners
        notifyDataSetChanged()
    }
}