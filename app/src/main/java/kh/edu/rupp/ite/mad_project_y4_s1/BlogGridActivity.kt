package kh.edu.rupp.ite.mad_project_y4_s1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class BlogGridActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("BlogGridActivity", "onCreate called")
        setContentView(R.layout.blog_grid)
    }
}