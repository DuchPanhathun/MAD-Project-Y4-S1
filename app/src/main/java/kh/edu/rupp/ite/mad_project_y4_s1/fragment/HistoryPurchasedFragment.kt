package kh.edu.rupp.ite.mad_project_y4_s1.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.HistoryPurchaseAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.model.HistoryPurchaseItem

class HistoryPurchasedFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateText: TextView
    private val historyAdapter = HistoryPurchaseAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_purchased, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        recyclerView = view.findViewById(R.id.historyRecyclerView)
        emptyStateText = view.findViewById(R.id.emptyHistoryText)
        
        setupRecyclerView()
        loadPurchaseHistory()
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }
    }

    private fun loadPurchaseHistory() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("purchaseHistory")
                .orderBy("purchaseDate", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.e("HistoryPurchased", "Error loading history", e)
                        return@addSnapshotListener
                    }

                    val historyItems = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(HistoryPurchaseItem::class.java)
                    } ?: emptyList()

                    if (historyItems.isEmpty()) {
                        recyclerView.visibility = View.GONE
                        emptyStateText.visibility = View.VISIBLE
                    } else {
                        recyclerView.visibility = View.VISIBLE
                        emptyStateText.visibility = View.GONE
                        historyAdapter.updateHistory(historyItems)
                    }
                }
        }
    }
} 