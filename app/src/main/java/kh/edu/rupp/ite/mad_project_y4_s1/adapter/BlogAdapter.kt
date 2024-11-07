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

class BlogAdapter(
<<<<<<< HEAD
    private val blogs: List<Blog>,
    private val onBlogClick: (Blog) -> Unit
) : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {
=======
    private var blogs: List<Blog>,
    private val onBlogClick: (Blog) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_BLOG = 0
        private const val VIEW_TYPE_FOOTER = 1
    }
>>>>>>> origin/thun

    class BlogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coverImage: ImageView = view.findViewById(R.id.blogCoverImage)
        val title: TextView = view.findViewById(R.id.blogTitle)
    }

    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Initialize footer views here if needed
    }

<<<<<<< HEAD
    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val blog = blogs[position]
        holder.title.text = blog.title
        Glide.with(holder.itemView.context)
            .load(blog.coverImage)
            .into(holder.coverImage)

        holder.itemView.setOnClickListener {
            onBlogClick(blog)
        }
    }

    override fun getItemCount() = blogs.size
}
=======
    override fun getItemViewType(position: Int): Int {
        return if (position == blogs.size) VIEW_TYPE_FOOTER else VIEW_TYPE_BLOG
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FOOTER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.footer_layout, parent, false)
            FooterViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_blog, parent, false)
            BlogViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BlogViewHolder -> {
                val blog = blogs[position]
                holder.title.text = blog.title
                Glide.with(holder.itemView.context)
                    .load(blog.coverImage)
                    .into(holder.coverImage)
                holder.itemView.setOnClickListener { onBlogClick(blog) }
            }
            is FooterViewHolder -> {
                // Bind footer data if needed
            }
        }
    }

    override fun getItemCount() = blogs.size + 1

    fun updateBlogs(newBlogs: List<Blog>) {
        blogs = newBlogs
        notifyDataSetChanged()
    }
}
>>>>>>> origin/thun
