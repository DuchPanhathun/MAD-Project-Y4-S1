package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.BlogAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.api.ApiState
import kh.edu.rupp.ite.mad_project_y4_s1.model.BlogItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class BlogGridActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val itemsState = MutableStateFlow<ApiState<List<BlogItem>>>(ApiState.Loading)
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("BlogGridActivity", "onCreate called")
        setContentView(R.layout.blog_grid)

        recyclerView = findViewById(R.id.itemsRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        setupRecyclerView()
        fetchItems()
        observeState()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchItems() {
        lifecycleScope.launch {
            try {
                val snapshot = db.collection("items").get().await()
                val items = snapshot.toObjects(BlogItem::class.java)
                Log.d("BlogGridActivity", "Fetched items: $items") // Log fetched items
                itemsState.emit(ApiState.Success(items))
            } catch (e: Exception) {
                Log.e("BlogGridActivity", "Error fetching items: ${e.message}") // Log errors
                itemsState.emit(ApiState.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }


    private fun observeState() {
        lifecycleScope.launch {
            itemsState.collect { state ->
                when (state) {
                    is ApiState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is ApiState.Success -> {
                        progressBar.visibility = View.GONE
                        recyclerView.adapter = BlogAdapter(state.data)
                    }
                    is ApiState.Error -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@BlogGridActivity, state.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
