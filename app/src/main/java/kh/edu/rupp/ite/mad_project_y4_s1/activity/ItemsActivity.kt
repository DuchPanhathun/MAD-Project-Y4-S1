package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.ItemsAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.api.ApiState
import kh.edu.rupp.ite.mad_project_y4_s1.model.Item

class ItemsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val itemsState = MutableStateFlow<ApiState<List<Item>>>(ApiState.Loading)
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

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
                val items = snapshot.toObjects(Item::class.java)
                itemsState.emit(ApiState.Success(items))
            } catch (e: Exception) {
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
                        recyclerView.adapter = ItemsAdapter(state.data)
                    }
                    is ApiState.Error -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@ItemsActivity, state.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}