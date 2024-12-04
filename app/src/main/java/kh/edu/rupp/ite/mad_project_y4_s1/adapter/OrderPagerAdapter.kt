package kh.edu.rupp.ite.mad_project_y4_s1.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kh.edu.rupp.ite.mad_project_y4_s1.fragment.PurchasedFragment
import kh.edu.rupp.ite.mad_project_y4_s1.fragment.FavoritesFragment
import kh.edu.rupp.ite.mad_project_y4_s1.fragment.HistoryPurchasedFragment

class OrderPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PurchasedFragment()
            1 -> HistoryPurchasedFragment()
            2 -> FavoritesFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
} 