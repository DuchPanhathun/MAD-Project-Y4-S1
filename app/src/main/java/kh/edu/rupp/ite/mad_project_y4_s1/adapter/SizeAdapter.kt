package kh.edu.rupp.ite.mad_project_y4_s1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kh.edu.rupp.ite.mad_project_y4_s1.R

class SizeAdapter(
    private val sizes: List<String>,
    private var selectedPosition: Int = -1
) : RecyclerView.Adapter<SizeAdapter.SizeViewHolder>() {

    private var onSizeSelectedListener: ((String) -> Unit)? = null

    fun setOnSizeSelectedListener(listener: (String) -> Unit) {
        onSizeSelectedListener = listener
    }

    inner class SizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(size: String, position: Int) {
            (itemView as TextView).text = size
            itemView.isSelected = position == selectedPosition
            
            itemView.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)
                onSizeSelectedListener?.invoke(size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_size, parent, false)
        return SizeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        holder.bind(sizes[position], position)
    }

    override fun getItemCount() = sizes.size
}