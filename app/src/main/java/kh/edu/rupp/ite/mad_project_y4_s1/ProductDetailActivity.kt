package kh.edu.rupp.ite.mad_project_y4_s1

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail)

        val viewPager: ViewPager2 = findViewById(R.id.productImageCarousel)
        val tabLayout: TabLayout = findViewById(R.id.indicator)

        // Assume you have a list of image resources
        val images = listOf(R.drawable.cover_image1, R.drawable.cover_image2, R.drawable.cover_image3)

        viewPager.adapter = ImagePagerAdapter(images)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // You can customize the tab here if needed
        }.attach()
    }
}

class ImagePagerAdapter(private val images: List<Int>) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    class ImageViewHolder(private val imageView: ImageView) : RecyclerView.ViewHolder(imageView) {
        fun bind(imageResId: Int) {
            imageView.setImageResource(imageResId)
        }
    }
}