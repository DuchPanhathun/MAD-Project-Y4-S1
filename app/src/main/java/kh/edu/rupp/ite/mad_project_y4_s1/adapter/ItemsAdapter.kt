package kh.edu.rupp.ite.mad_project_y4_s1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.model.Item

class ItemsAdapter(private val items: List<Item>) :
    RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val brandName: TextView = view.findViewById(R.id.brandNameText)
        val type: TextView = view.findViewById(R.id.typeText)
        val price: TextView = view.findViewById(R.id.priceText)
        val size: TextView = view.findViewById(R.id.sizeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.brandName.text = item.brandName
        holder.type.text = item.type
        holder.price.text = "$${item.price}"
        holder.size.text = "Size: ${item.size}"
    }

    override fun getItemCount() = items.size
}