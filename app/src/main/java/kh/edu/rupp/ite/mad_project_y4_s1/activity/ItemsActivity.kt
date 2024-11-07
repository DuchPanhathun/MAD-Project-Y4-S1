package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.viewModels
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.ItemsAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiState
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.ItemsViewModel

class ItemsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val viewModel: ItemsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        recyclerView = findViewById(R.id.itemsRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        setupRecyclerView()
        observeState()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeState() {
        viewModel.itemsState.observe(this) { response ->
            when (response.status) {
                ApiState.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                ApiState.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    response.data?.let { items ->
                        recyclerView.adapter = ItemsAdapter(items)
                    }
                }
                ApiState.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, response.error ?: "Error loading items", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}