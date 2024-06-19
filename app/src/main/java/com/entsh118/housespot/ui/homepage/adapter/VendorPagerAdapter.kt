package com.entsh118.housespot.ui.homepage.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.entsh118.housespot.ui.homepage.fragments.ActiveOrdersFragment
import com.entsh118.housespot.ui.homepage.fragments.OrderHistoryFragment

class VendorPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            ActiveOrdersFragment()
        } else {
            OrderHistoryFragment()
        }
    }
}
