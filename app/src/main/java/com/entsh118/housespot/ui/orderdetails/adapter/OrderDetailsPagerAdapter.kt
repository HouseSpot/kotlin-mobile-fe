package com.entsh118.housespot.ui.orderdetails.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.entsh118.housespot.ui.orderdetails.fragments.InformasiProyekFragment
import com.entsh118.housespot.ui.orderdetails.fragments.ProgressProyekFragment

class OrderDetailsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            ProgressProyekFragment()
        } else {
            InformasiProyekFragment()
        }
    }
}
