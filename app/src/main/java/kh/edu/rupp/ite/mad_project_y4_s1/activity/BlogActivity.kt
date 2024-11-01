package kh.edu.rupp.ite.mad_project_y4_s1.activity
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 27b5cc5 (Blog post)

import android.content.Intent
import android.os.Bundle
<<<<<<< HEAD
import kh.edu.rupp.ite.mad_project_y4_s1.R
=======
>>>>>>> fa060a4 (fix stash)
=======
import android.content.Intent
import android.os.Bundle
>>>>>>> 14e41be (blog post)
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.BlogAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiResponse
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiState
<<<<<<< HEAD
<<<<<<< HEAD
import kh.edu.rupp.ite.mad_project_y4_s1.model.Blog
=======
>>>>>>> 14e41be (blog post)
=======
import kh.edu.rupp.ite.mad_project_y4_s1.model.Blog
>>>>>>> 27b5cc5 (Blog post)
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
<<<<<<< HEAD
=======
=======

<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> 0d79057 (..)
=======
<<<<<<< HEAD
>>>>>>> 0c597ef (fix stash)
=======
<<<<<<< HEAD
>>>>>>> 290c922 (bloggrid done)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
>>>>>>> b268321 (..)

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        recyclerView.layoutManager = GridLayoutManager(this, 1)

=======
>>>>>>> 215843c (fix stash)
=======
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        
>>>>>>> cadd3f4 (fix stash)
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 8a481e1 (bloggrid done)
=======
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        
>>>>>>> 90e3bc1 (bloggrid done)
<<<<<<< HEAD
=======
        recyclerView.layoutManager = GridLayoutManager(this, 2)

>>>>>>> 14e41be (blog post)
=======
        recyclerView.layoutManager = GridLayoutManager(this, 2)

>>>>>>> 27b5cc5 (Blog post)
=======
>>>>>>> 7b2ead3 (fix stash)
=======
>>>>>>> 215843c (fix stash)
=======
>>>>>>> 8a481e1 (bloggrid done)
        lifecycleScope.launch {
            viewModel.blogsState.collect { response: ApiResponse<List<Blog>> ->
                when (response.status) {
                    ApiState.LOADING -> progressBar.visibility = View.VISIBLE
                    ApiState.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        response.data?.let { blogs ->
                            recyclerView.adapter = BlogAdapter(blogs) { blog ->
                                val intent = Intent(this@BlogActivity, BlogDetailActivity::class.java).apply {
                                    putExtra("blog", blog)
                                }
                                startActivity(intent)
                            }
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
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
} 
=======
}
>>>>>>> 14e41be (blog post)
=======
}
>>>>>>> 27b5cc5 (Blog post)
=======
} 
>>>>>>> 215843c (fix stash)
