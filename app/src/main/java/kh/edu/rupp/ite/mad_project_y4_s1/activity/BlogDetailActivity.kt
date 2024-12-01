package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.AdditionalPhotosAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.model.Blog
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.BlogViewModel
import androidx.activity.viewModels

class BlogDetailActivity : AppCompatActivity() {

    private val viewModel: BlogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_detail)

        // Get blog from intent and set it in ViewModel
        intent.getParcelableExtra<Blog>("blog")?.let { blog ->
            viewModel.setSelectedBlog(blog)
        }

        // Observe selected blog
        viewModel.selectedBlog.observe(this) { blog ->
            findViewById<TextView>(R.id.titleText).text = blog.title
            findViewById<TextView>(R.id.detailText).text = blog.detail
            findViewById<TextView>(R.id.additionalDetailsText).text = blog.additionalDetails

            Glide.with(this)
                .load(blog.coverImage)
                .into(findViewById<ImageView>(R.id.coverImage))

            // Setup additional photos recycler view
            val photosRecyclerView = findViewById<RecyclerView>(R.id.additionalPhotosRecyclerView)
            photosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            photosRecyclerView.adapter = AdditionalPhotosAdapter(blog.additionalPhotos)
        }
    }
}