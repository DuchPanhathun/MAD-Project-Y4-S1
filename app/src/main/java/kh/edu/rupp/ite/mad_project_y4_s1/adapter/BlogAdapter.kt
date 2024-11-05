package kh.edu.rupp.ite.mad_project_y4_s1.adapter
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> 14e41be (blog post)
=======

>>>>>>> 27b5cc5 (Blog post)
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kh.edu.rupp.ite.mad_project_y4_s1.R
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 27b5cc5 (Blog post)
import kh.edu.rupp.ite.mad_project_y4_s1.model.Blog

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
class BlogAdapter(private var blogs: List<Blog>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_BLOG = 0
        private const val VIEW_TYPE_FOOTER = 1
    }
=======
class BlogAdapter(private val blogs: List<Blog>) : 
    RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {
>>>>>>> cadd3f4 (fix stash)
=======
class BlogAdapter(private val blogs: List<Blog>) : 
    RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {
>>>>>>> 90e3bc1 (bloggrid done)
=======
=======

>>>>>>> 14e41be (blog post)
class BlogAdapter(
    private var blogs: List<Blog>,
    private val onBlogClick: (Blog) -> Unit
<<<<<<< HEAD
) : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> f0a2911 (...)
=======
>>>>>>> 14e41be (blog post)
=======
>>>>>>> 27b5cc5 (Blog post)
=======
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_BLOG = 0
        private const val VIEW_TYPE_FOOTER = 1
    }
>>>>>>> 7b2ead3 (fix stash)

    class BlogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coverImage: ImageView = view.findViewById(R.id.blogCoverImage)
        val title: TextView = view.findViewById(R.id.blogTitle)
    }

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Initialize footer views here if needed
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == blogs.size) {
            VIEW_TYPE_FOOTER // Return footer view type for the last position
        } else {
            VIEW_TYPE_BLOG // Return blog view type
        }
    }

<<<<<<< HEAD
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FOOTER) {
            // Inflate footer layout here
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.footer_layout, parent, false) // Your footer layout
            FooterViewHolder(view)
        } else {
            // Inflate blog item layout here
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_blog, parent, false)
            BlogViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BlogViewHolder) {
            val blog = blogs[position]
            holder.title.text = blog.title
            Glide.with(holder.itemView.context)
                .load(blog.coverImage)
                .into(holder.coverImage)
        } else if (holder is FooterViewHolder) {
            // Bind footer data if needed
            // You can also set up click listeners or other logic for the footer here
        }
    }

    override fun getItemCount(): Int {
        return blogs.size + 1 // Add one for the footer
    }

    // Method to update the blogs and notify the adapter
    fun updateBlogs(newBlogs: List<Blog>) {
        blogs = newBlogs
        notifyDataSetChanged()
    }
}
=======
    override fun getItemCount() = blogs.size
} 
>>>>>>> cadd3f4 (fix stash)
=======
=======
>>>>>>> 14e41be (blog post)
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
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
            
=======
=======
>>>>>>> 27b5cc5 (Blog post)

>>>>>>> 14e41be (blog post)
=======
>>>>>>> 8ccd66b (..)
        holder.itemView.setOnClickListener {
            onBlogClick(blog)
        }
    }

    override fun getItemCount() = blogs.size
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
} 
<<<<<<< HEAD
>>>>>>> 90e3bc1 (bloggrid done)
=======
>>>>>>> refs/remotes/origin/thun
>>>>>>> 86c7f6a (sdfsdf)
=======
} 
>>>>>>> fa060a4 (fix stash)
=======
=======
    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Initialize footer views here if needed
    }

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
>>>>>>> 7b2ead3 (fix stash)
}
>>>>>>> 14e41be (blog post)
=======
}
>>>>>>> 27b5cc5 (Blog post)
