package com.chidi.mystockapp.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chidi.mystockapp.databinding.ItemStockBinding
import com.chidi.mystockapp.domain.StockItem
import kotlin.math.abs


class StockListingAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<StockListingAdapter.StockViewHolder>() {

    private var stockList = mutableListOf<StockItem>()
    private var stockListFull = mutableListOf<StockItem>()

    inner class StockViewHolder(private val binding: ItemStockBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        @Synchronized
        fun bind(stockItem: StockItem, position: Int) {
            binding.stock = stockItem
            binding.run {
                stockCompanyName.text = stockItem.name
                stockTicker.text = stockItem.ticker
                val stockDelta = stockItem.latestPrice - stockItem.previousClose
                val stockRisePercent = abs(stockDelta) / stockItem.previousClose * 100
                if (stockDelta < 0) {
                    stockDayDelta.setTextColor(Color.RED)
                    stockDayDelta.text = "-$%.2f (%.2f".format(-stockDelta, stockRisePercent) + "%)"
                } else {
                    stockDayDelta.text = "+$%.2f (%.2f".format(stockDelta, stockRisePercent) + "%)"
                    stockDayDelta.setTextColor(Color.GREEN)
                }

                binding.root.setOnClickListener {
                    listener.onItemClick(stockItem)
                }
            }
            binding.executePendingBindings()
        }
    }

    fun submitList(stockList: List<StockItem>) {
        this.stockList = stockList as MutableList<StockItem>
        this.stockListFull = ArrayList(this.stockList)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(stockItem: StockItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = stockList[position]
        holder.bind(stock, position)
    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    companion object DiffCallback : DiffUtil.ItemCallback<StockItem>() {
        override fun areItemsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem.ticker == newItem.ticker
        }
    }
}