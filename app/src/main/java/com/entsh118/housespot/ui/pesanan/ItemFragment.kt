package com.entsh118.housespot.ui.pesanan

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.entsh118.housespot.R
import com.entsh118.housespot.data.api.response.DataItem
import com.entsh118.housespot.databinding.FragmentItemListBinding
import com.entsh118.housespot.databinding.FragmentPesananBinding
import com.entsh118.housespot.ui.pesanan.adapter.ListPesananAdapter
import com.entsh118.housespot.ui.pesanan.adapter.ListPesananPasifAdapter
import com.entsh118.housespot.ui.pesanan.viewmodel.PesananViewModel

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {

    private lateinit var viewModel: PesananViewModel
    private lateinit var binding: FragmentPesananBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[PesananViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using view binding
        binding = FragmentPesananBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val ARG_POSITION = "position"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var position = 0

        arguments?.let {
            position = it.getInt(ARG_POSITION)
        }
         //   viewModel.getPesananAktif()
     //   viewModel.getPesananAktif(id)
        val rv = binding.rvFollow

        if (position == 1){
            viewModel.listPesananAktif.observe(viewLifecycleOwner) { listPesananAktif ->
                if (listPesananAktif != null) {
                    if (listPesananAktif.isNotEmpty()) {
                        rv.layoutManager = LinearLayoutManager(activity)
                        listPesananAktif?.let { setListPesananAktif(it, rv) }
                    }
                }
            }
        } else {
            viewModel.listPesananPasif.observe(viewLifecycleOwner) { listPesananPasif ->
                if (listPesananPasif != null) {
                    if (listPesananPasif.isNotEmpty()) {
                        rv.layoutManager = LinearLayoutManager(activity)
                        listPesananPasif?.let { setListRiwayatPesanan(it, rv) }
                    }
                }
            }
        }
    }

   private fun setListPesananAktif(list: List<DataItem?>, rv: RecyclerView){
        val adapter = ListPesananAdapter(list)
        rv.adapter = adapter
    }

    private fun setListRiwayatPesanan(list: List<DataItem?>, rv: RecyclerView){
        val adapter = ListPesananPasifAdapter(list)
        rv.adapter = adapter
    }


}