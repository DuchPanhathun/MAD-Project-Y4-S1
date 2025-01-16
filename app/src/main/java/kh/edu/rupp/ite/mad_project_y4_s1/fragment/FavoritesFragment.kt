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
import kh.edu.rupp.ite.mad_project_y4_s1.adapter.FavoritesAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.FavoritesViewModel
import android.content.Intent
import kh.edu.rupp.ite.mad_project_y4_s1.activity.ItemDetailActivity
import kh.edu.rupp.ite.mad_project_y4_s1.model.Item

class FavoritesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateText: TextView
    private val favoritesAdapter = FavoritesAdapter()
    private val viewModel: FavoritesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        Log.d("FavoritesFragment", "Fragment created")
        
        recyclerView = view.findViewById(R.id.favoritesRecyclerView)
        emptyStateText = view.findViewById(R.id.emptyStateText)

        setupRecyclerView()
        observeFavorites()
    }

    private fun setupRecyclerView() {
        Log.d("FavoritesFragment", "Setting up RecyclerView")
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoritesAdapter
        }
        favoritesAdapter.setOnItemClick { favoriteItem ->
            val intent = Intent(requireContext(), ItemDetailActivity::class.java)
            intent.putExtra("item", Item(
                images = listOf(favoriteItem.imageUrl),
                brandName = favoriteItem.brandName,
                type = favoriteItem.type,
                price = favoriteItem.price,
                sizes = favoriteItem.sizes,
                colors = favoriteItem.colors,
                materialDetail = "",
                additionalCareDetails = "",
                careDetails = listOf(),
                deliveryStartDate = "",
                deliveryEndDate = ""
            ))
            startActivity(intent)
        }
    }

    private fun observeFavorites() {
        Log.d("FavoritesFragment", "Starting to observe favorites")
        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            Log.d("FavoritesFragment", "Received favorites update: $favorites")
            
            if (favorites.isEmpty()) {
                recyclerView.visibility = View.GONE
                emptyStateText.visibility = View.VISIBLE
                Log.d("FavoritesFragment", "Showing empty state")
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyStateText.visibility = View.GONE
                favoritesAdapter.updateFavorites(favorites)
                Log.d("FavoritesFragment", "Updated adapter with favorites")
            }
        }
    }
} 