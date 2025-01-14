package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.ItemsAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiState
import kh.edu.rupp.ite.mad_project_y4_s1.model.Item
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.ItemsViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var adapter: ItemsAdapter
    private lateinit var noResultsView: View
    private lateinit var resultsCount: TextView

    private val items = mutableListOf<Item>()
    private val viewModel: ItemsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Initialize views
        searchEditText = findViewById(R.id.searchEditText)
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView)
        noResultsView = findViewById(R.id.noResultsView)
        resultsCount = findViewById(R.id.resultsCount)

        // Setup RecyclerView
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ItemsAdapter(emptyList()) { item ->
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("item", item)
            startActivity(intent)
        }
        searchResultsRecyclerView.adapter = adapter

        // Load initial items
        loadItems()


        // Load initial items
        loadItems()

        // Setup search functionality
        searchEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(v.text.toString())
                true
            } else {
                false
            }
        }

        findViewById<ImageButton>(R.id.searchActionButton).setOnClickListener {
            performSearch(searchEditText.text.toString())
        }

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }
    }

    private fun loadItems() {
        viewModel.itemsState.observe(this) { response ->
            when (response.status) {
                ApiState.SUCCESS -> {
                    response.data?.let { loadedItems ->
                        items.clear()
                        items.addAll(loadedItems)
                    }
                }
                ApiState.ERROR -> {
                    Toast.makeText(this, response.error ?: "Error loading items", Toast.LENGTH_LONG).show()
                }
                ApiState.LOADING -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    private fun performSearch(query: String) {
        // Hide keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)


        // Perform search using brandName, type, and materialDetail
        val filteredItems = items.filter { item ->
            item.brandName.contains(query, ignoreCase = true) ||
                    item.type.contains(query, ignoreCase = true) ||
                    item.materialDetail.contains(query, ignoreCase = true)
        }

        // Update UI based on results
        if (filteredItems.isEmpty()) {
            noResultsView.visibility = View.VISIBLE
            searchResultsRecyclerView.visibility = View.GONE
            resultsCount.visibility = View.GONE
        } else {
            noResultsView.visibility = View.GONE
            searchResultsRecyclerView.visibility = View.VISIBLE
            resultsCount.visibility = View.VISIBLE
            resultsCount.text = "${filteredItems.size} results found"
            adapter.submitList(filteredItems)
        }
    }
}
