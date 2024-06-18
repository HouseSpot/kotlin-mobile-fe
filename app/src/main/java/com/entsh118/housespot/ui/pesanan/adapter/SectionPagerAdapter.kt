package com.entsh118.housespot.ui.pesanan.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.entsh118.housespot.ui.pesanan.ItemFragment
import android.os.Bundle

class SectionPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    var username: String = ""
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        val fragment = ItemFragment()
        fragment.arguments = Bundle().apply {
            putInt(ItemFragment.ARG_POSITION, position + 1)
        }
        return fragment
    }
}