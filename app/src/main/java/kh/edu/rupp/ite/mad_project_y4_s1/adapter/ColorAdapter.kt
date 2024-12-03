package kh.edu.rupp.ite.mad_project_y4_s1.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kh.edu.rupp.ite.mad_project_y4_s1.R

class ColorAdapter(private val colors: List<String>) : 
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val colorCircle: View = view.findViewById(R.id.colorCircle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = colors[position]
        try {
            // Parse the color code and set it as the background
            val colorInt = Color.parseColor(color)
            holder.colorCircle.background.setColorFilter(colorInt, PorterDuff.Mode.SRC_IN)
        } catch (e: IllegalArgumentException) {
            // Handle invalid color codes
            Log.e("ColorAdapter", "Invalid color code: $color")
        }
    }

    override fun getItemCount() = colors.size
}