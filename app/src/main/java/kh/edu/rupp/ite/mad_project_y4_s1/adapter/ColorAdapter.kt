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

    private var selectedPosition = -1
    private var onColorSelectedListener: ((String) -> Unit)? = null

    fun setOnColorSelectedListener(listener: (String) -> Unit) {
        onColorSelectedListener = listener
    }

    class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val colorCircle: View = view.findViewById(R.id.colorCircle)
        val strokeCircle: View = view.findViewById(R.id.strokeCircle) // Add this view for stroke
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = colors[position]
        try {
            val colorInt = Color.parseColor(color)
            holder.colorCircle.background.setColorFilter(colorInt, PorterDuff.Mode.SRC_IN)
            
            // Show/hide stroke based on selection
            holder.strokeCircle.visibility = if (position == selectedPosition) View.VISIBLE else View.INVISIBLE

            // Handle click
            holder.itemView.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)
                onColorSelectedListener?.invoke(color)
            }
        } catch (e: IllegalArgumentException) {
            Log.e("ColorAdapter", "Invalid color code: $color")
        }
    }

    override fun getItemCount() = colors.size
}