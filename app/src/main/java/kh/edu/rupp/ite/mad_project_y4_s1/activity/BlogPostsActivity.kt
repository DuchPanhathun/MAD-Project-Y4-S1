//package kh.edu.rupp.ite.mad_project_y4_s1.activity
//
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.ImageView
//import android.widget.ProgressBar
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import kh.edu.rupp.ite.mad_project_y4_s1.R
//import kh.edu.rupp.ite.mad_project_y4_s1.adapter.BlogAdapter
//import kh.edu.rupp.ite.mad_project_y4_s1.model.BlogItem
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//
//class BlogPostsActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var progressBar: ProgressBar
//    private val db = FirebaseFirestore.getInstance()
//    private val auth = FirebaseAuth.getInstance() // FirebaseAuth instance for authentication
//
//    private lateinit var blogImage: ImageView
//    private lateinit var textBlog: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.d("BlogPostsActivity", "Setting content view to blog_grid")
//        setContentView(R.layout.blog_grid)
//        Log.d("BlogPostsActivity", "Content view set")
//
//        // Initialize views
//        recyclerView = findViewById(R.id.itemsRecyclerView) ?: run {
//            Log.e("BlogPostsActivity", "RecyclerView not found in blog_grid layout")
//            return
//        }
//        progressBar = findViewById(R.id.progressBar) ?: run {
//            Log.e("BlogPostsActivity", "ProgressBar not found in blog_grid layout")
//            return
//        }
//
//        // Set up RecyclerView
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        // Fetch blog posts from Firestore
//        fetchBlogPosts()
//    }
//
//
//    private fun fetchBlogPosts() {
//        // Show ProgressBar while loading data
//        progressBar.visibility = View.VISIBLE
//        recyclerView.visibility = View.GONE // Hide RecyclerView while loading
//
//        lifecycleScope.launch {
//            try {
//                // Fetch data from Firestore
//                val snapshot = db.collection("blogPosts").get().await()
//                val blogPosts = snapshot.toObjects(BlogItem::class.java)
//
//                // Set the adapter with the fetched data
//                recyclerView.adapter = BlogAdapter(blogPosts) { blogItem ->
//                    // Handle item clicks if needed
//                    Toast.makeText(this@BlogPostsActivity, "Clicked: ${blogItem.title}", Toast.LENGTH_SHORT).show()
//                }
//
//                // Hide ProgressBar and show RecyclerView after loading
//                progressBar.visibility = View.GONE
//                recyclerView.visibility = View.VISIBLE
//            } catch (e: Exception) {
//                Toast.makeText(this@BlogPostsActivity, "Failed to load blog posts", Toast.LENGTH_LONG).show()
//                Log.e("BlogPostsActivity", "Error fetching blog posts", e)
//
//                // Ensure ProgressBar is hidden in case of an error
//                progressBar.visibility = View.GONE
//                recyclerView.visibility = View.GONE
//            }
//        }
//    }
//}
