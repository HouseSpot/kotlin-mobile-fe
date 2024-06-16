package com.entsh118.housespot.ui.orderdetails.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.entsh118.housespot.databinding.FragmentInformasiProyekBinding
import com.entsh118.housespot.ui.orderdetails.viewmodel.OrderDetailsViewModel

class InformasiProyekFragment : Fragment() {

    private var _binding: FragmentInformasiProyekBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrderDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInformasiProyekBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.order.observe(viewLifecycleOwner, { order ->
            // Update UI with order details
            binding.tvJenisLayanan.text = order.serviceType
            binding.tvJenisProperti.setText(order.propertyType)
        })

        // Set up other UI components and listeners
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
