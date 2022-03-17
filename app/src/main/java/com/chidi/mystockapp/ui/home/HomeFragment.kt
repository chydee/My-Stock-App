package com.chidi.mystockapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chidi.mystockapp.R
import com.chidi.mystockapp.databinding.HomeFragmentBinding
import com.chidi.mystockapp.domain.StockItem
import com.chidi.mystockapp.ui.MainActivity
import com.chidi.mystockapp.ui.MainViewModel
import com.chidi.mystockapp.ui.utilities.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment), StockListingAdapter.OnItemClickListener {


    private val binding by viewBinding(HomeFragmentBinding::bind)
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: StockListingAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureActionBar()
        setupRecyclerView()

        viewModel.stockMap.observe(viewLifecycleOwner) { map ->
            binding.progressLoader.visibility = View.GONE
            mainViewModel.setStockMap(map)
        }

        mainViewModel.stockMap.observe(viewLifecycleOwner) {
            val stockList = it.values.toList()
            adapter.submitList(stockList)
            adapter.notifyDataSetChanged()
        }

        viewModel.configureStockMap()
    }

    override fun onItemClick(stockItem: StockItem) {
        val action = HomeFragmentDirections.toDetailFragment(stockItem)
        findNavController().navigate(action)
    }

    private fun configureActionBar() {
        (activity as MainActivity).supportActionBar?.subtitle = "Today's Stock Listing"
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(null)
    }

    private fun setupRecyclerView() {
        binding.stockListingRecyclerView.adapter = StockListingAdapter(this)
        binding.stockListingRecyclerView.setHasFixedSize(false)

        adapter = binding.stockListingRecyclerView.adapter as StockListingAdapter
    }

}