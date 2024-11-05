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

class BlogDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_detail)

        val blog = intent.getParcelableExtra<Blog>("blog")
        blog?.let {
            findViewById<TextView>(R.id.titleText).text = it.title
            findViewById<TextView>(R.id.detailText).text = it.detail
            findViewById<TextView>(R.id.additionalDetailsText).text = it.additionalDetails

            Glide.with(this)
                .load(it.coverImage)
                .into(findViewById<ImageView>(R.id.coverImage))

            // Setup additional photos recycler view
            val photosRecyclerView = findViewById<RecyclerView>(R.id.additionalPhotosRecyclerView)
            photosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            photosRecyclerView.adapter = AdditionalPhotosAdapter(it.additionalPhotos)
        }
    }
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 042a0ba (...)
=======
>>>>>>> 469b0de (...)
} 
=======
}
>>>>>>> cd6fae2 (...)
<<<<<<< HEAD
<<<<<<< HEAD
=======
} 
>>>>>>> d92960f (...)
=======
>>>>>>> 042a0ba (...)
=======
=======
} 
>>>>>>> d92960f (...)
>>>>>>> 469b0de (...)
