package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.BlogAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiResponse
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiState
import kh.edu.rupp.ite.mad_project_y4_s1.model.Blog
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.BlogViewModel
import kotlinx.coroutines.launch

class BlogActivity : AppCompatActivity() {
    private val viewModel: BlogViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)

        recyclerView = findViewById(R.id.blogRecyclerView)
        progressBar = findViewById(R.id.progressBar)

<<<<<<< HEAD
        recyclerView.layoutManager = GridLayoutManager(this, 1)

=======
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        
>>>>>>> cadd3f4 (fix stash)
        lifecycleScope.launch {
            viewModel.blogsState.collect { response: ApiResponse<List<Blog>> ->
                when (response.status) {
                    ApiState.LOADING -> progressBar.visibility = View.VISIBLE
                    ApiState.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        response.data?.let { blogs ->
                            recyclerView.adapter = BlogAdapter(blogs)
                        }
                    }
                    ApiState.ERROR -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@BlogActivity, response.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
} 