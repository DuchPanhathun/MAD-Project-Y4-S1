package kh.edu.rupp.ite.mad_project_y4_s1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.model.Blog

class BlogAdapter(private val blogs: List<Blog>) :
    RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    class BlogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coverImage: ImageView = view.findViewById(R.id.blogCoverImage)
        val title: TextView = view.findViewById(R.id.blogTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_blog, parent, false)
        return BlogViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val blog = blogs[position]
        holder.title.text = blog.title
        Glide.with(holder.itemView.context)
            .load(blog.coverImage)
            .into(holder.coverImage)
    }

    override fun getItemCount() = blogs.size
    fun updateBlogs(blogs: List<Blog>) {

    }
}