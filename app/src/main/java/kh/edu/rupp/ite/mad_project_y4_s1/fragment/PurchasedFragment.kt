package kh.edu.rupp.ite.mad_project_y4_s1.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.PurchasedAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.PurchasedViewModel

class PurchasedFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateText: TextView
    private val purchasedAdapter = PurchasedAdapter()
    private val viewModel: PurchasedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_purchased, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        Log.d("PurchasedFragment", "Fragment created")
        
        recyclerView = view.findViewById(R.id.purchasedRecyclerView)
        emptyStateText = view.findViewById(R.id.emptyStateText)

        setupRecyclerView()
        observePurchased()
    }

    private fun setupRecyclerView() {
        Log.d("PurchasedFragment", "Setting up RecyclerView")
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = purchasedAdapter
        }
    }

    private fun observePurchased() {
        Log.d("PurchasedFragment", "Starting to observe purchased items")
        viewModel.purchased.observe(viewLifecycleOwner) { purchased ->
            Log.d("PurchasedFragment", "Received purchased update: $purchased")
            
            if (purchased.isEmpty()) {
                recyclerView.visibility = View.GONE
                emptyStateText.visibility = View.VISIBLE
                Log.d("PurchasedFragment", "Showing empty state")
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyStateText.visibility = View.GONE
                purchasedAdapter.updatePurchased(purchased)
                Log.d("PurchasedFragment", "Updated adapter with purchased items")
            }
        }
    }
} 