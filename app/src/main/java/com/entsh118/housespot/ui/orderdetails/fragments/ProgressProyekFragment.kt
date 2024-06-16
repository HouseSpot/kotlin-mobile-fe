package com.entsh118.housespot.ui.orderdetails.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.entsh118.housespot.R
import com.entsh118.housespot.custom.StatusSpinnerAdapter
import com.entsh118.housespot.data.api.model.ReportLog
import com.entsh118.housespot.databinding.FragmentProgressProyekBinding
import com.entsh118.housespot.ui.orderdetails.adapter.ReportLogAdapter
import com.entsh118.housespot.ui.orderdetails.viewmodel.OrderDetailsViewModel

class ProgressProyekFragment : Fragment() {

    private var _binding: FragmentProgressProyekBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrderDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProgressProyekBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val statusArray = resources.getStringArray(R.array.status_array)
        val adapter = StatusSpinnerAdapter(requireContext(), statusArray)
        binding.spinnerStatus.adapter = adapter

        // Example data
        val reportLogs = listOf(
            ReportLog("12 Juni 2023", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", "https://example.com/image1.jpg"),
            ReportLog("13 Juni 2023", "Another report log without image.")
        )

        // Set up RecyclerView
        binding.recyclerViewReports.layoutManager = LinearLayoutManager(requireContext())
        val reportLogAdapter = ReportLogAdapter(reportLogs)
        binding.recyclerViewReports.adapter = reportLogAdapter

        // Show/hide empty state
        if (reportLogs.isEmpty()) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.recyclerViewReports.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.recyclerViewReports.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
