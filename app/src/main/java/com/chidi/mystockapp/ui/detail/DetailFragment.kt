package com.chidi.mystockapp.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.chidi.mystockapp.R
import com.chidi.mystockapp.databinding.DetailFragmentBinding
import com.chidi.mystockapp.domain.StockItem
import com.chidi.mystockapp.ui.MainActivity
import com.chidi.mystockapp.ui.utilities.viewBinding
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.detail_fragment) {

    private val binding by viewBinding(DetailFragmentBinding::bind)
    private val args: DetailFragmentArgs by navArgs()

    private  val viewModel: DetailViewModel by viewModels()

    private lateinit var stockItem: StockItem


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        stockItem = args.stockModel
        configureActionBar()
        viewModel.getCandles(stockItem, "W", 0)
        binding.apply {

            detailCurrentStockPrice.text = "$%.2f".format(stockItem.latestPrice)

            //calculate day delta and percent rise
            val stockDelta = stockItem.latestPrice - stockItem.previousClose
            val stockRisePercent = kotlin.math.abs(stockDelta) / stockItem.previousClose * 100
            if (stockDelta < 0) {
                detailStockDayDelta.setTextColor(Color.RED)
                detailStockDayDelta.text = "-$%.2f (%.2f".format(-stockDelta, stockRisePercent) + "%)"
                detailStockDayDelta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.round_trending_down_20, 0, 0, 0)
            } else {
                detailStockDayDelta.text = "+$%.2f (%.2f".format(stockDelta, stockRisePercent) + "%)"
                detailStockDayDelta.setTextColor(Color.GREEN)
                detailStockDayDelta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.round_trending_up_20, 0, 0, 0)
            }

            if (stockItem.logo.isEmpty()) {
                detailStockImage.setImageResource(R.drawable.trend)
            } else {
                detailStockImage.load(stockItem.logo) {
                    crossfade(true)
                    placeholder(R.drawable.trend)
                    transformations(CircleCropTransformation())
                }
            }
        }
        configureLineChart()
    }

    private fun configureActionBar() {
        (activity as MainActivity).supportActionBar?.title = stockItem.ticker
        (activity as MainActivity).supportActionBar?.subtitle = stockItem.name
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun configureLineChart() {
        viewModel.candle.observe(viewLifecycleOwner) { candle ->
            val entries = arrayListOf<Entry>()
            val closePriceList = candle.c
            var x = 0
            for (closePrice in closePriceList) {
                entries.add(Entry(x.toFloat(), closePrice.toFloat()))
                x += 10
            }
            val dataSet = LineDataSet(entries, stockItem.ticker)
            dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            dataSet.cubicIntensity= 0.2f
            dataSet.setDrawCircles(false)
            dataSet.setDrawValues(false)
            dataSet.color = R.color.blue
            dataSet.lineWidth = 1.8f

            dataSet.setDrawFilled(true)
            dataSet.fillDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.chart_bg)
            dataSet.circleRadius= 4f
            dataSet.highLightColor = Color.rgb(244, 117, 117);
            dataSet.setDrawHorizontalHighlightIndicator(false);
            dataSet.fillFormatter = IFillFormatter { _, _ -> binding.detailStockLineChart.axisLeft.axisMinimum }

            val lineData = LineData(dataSet)

            val lineChart = binding.detailStockLineChart

            //customizing
            lineChart.legend.isEnabled = false
            lineChart.setViewPortOffsets(0f, 0f, 0f, 0f);
            lineChart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));
            lineChart.setNoDataText("No Chart Data Available")
            lineChart.description.isEnabled = false
            lineChart.xAxis.isEnabled = false
            lineChart.axisLeft.isEnabled = false
            lineChart.axisRight.isEnabled = false

            // enable touch gestures
            lineChart.setTouchEnabled(true);
            // enable scaling and dragging
            lineChart.isDragEnabled = true
            lineChart.setScaleEnabled(true)

            // if disabled, scaling can be done on x- and y-axis separately
            lineChart.setPinchZoom(false)

            lineChart.setDrawGridBackground(false)
            lineChart.maxHighlightDistance = 300F

            val y: YAxis = lineChart.getAxisLeft()
            y.setLabelCount(6, false)
            y.textColor = Color.WHITE
            y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
            y.setDrawGridLines(false)
            y.axisLineColor = Color.WHITE

            lineChart.animateXY(2000, 2000);

            //setting data
            lineChart.data = lineData
            lineChart.invalidate()

        }
    }

}