package com.lightmicrofinance.commonproject.fragment


import android.graphics.Color
import android.os.Bundle
import android.view.*
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.lightmicrofinance.commonproject.databinding.FragmentHomeBinding


class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBarChart()
        showBarChart()

    }
    private fun showBarChart() {
        val valueList = ArrayList<Double>()
        val entries: ArrayList<BarEntry> = ArrayList()
        val title = "Title"

        //input data
        for (i in 0..5) {
            valueList.add(i * 100.1)
        }

        //fit the data into a bar
        for (i in 0 until valueList.size) {
            val barEntry = BarEntry(i.toFloat(), valueList[i].toFloat())
            entries.add(barEntry)
        }
        val barDataSet = BarDataSet(entries, title)
        val data = BarData(barDataSet)
        _binding?.barChartView?.setData(data)
        _binding?.barChartView?.invalidate()

        initBarDataSet(barDataSet);
    }

    private fun initBarDataSet(barDataSet: BarDataSet) {
        //Changing the color of the bar
        barDataSet.color = Color.parseColor("#304567")
        //Setting the size of the form in the legend
        barDataSet.formSize = 15f
        //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false)
        //setting the text size of the value of the bar
        barDataSet.valueTextSize = 12f

        //barDataSet.setColor( ContextCompat.getColor(this, R.color.savedColor));
    }

    private fun initBarChart() {
        //hiding the grey background of the chart, default false if not set
        _binding?.barChartView?.setDrawGridBackground(false)
        //remove the bar shadow, default false if not set
        _binding?.barChartView?.setDrawBarShadow(false)
        //remove border of the chart, default false if not set
        _binding?.barChartView?.setDrawBorders(false)

        //remove the description label text located at the lower right corner
        val description = Description()
        description.setEnabled(false)
        _binding?.barChartView?.setDescription(description)

        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        _binding?.barChartView?.animateY(1000)
        //setting animation for x-axis, the bar will pop up separately within the time we set
        _binding?.barChartView?.animateX(1000)
        val xAxis: XAxis =  _binding?.barChartView?.getXAxis()!!
        //change the position of x-axis to the bottom
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //set the horizontal distance of the grid line
        xAxis.granularity = 1f
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false)
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false)
        val leftAxis: YAxis =  _binding?.barChartView?.getAxisLeft()!!
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false)
        val rightAxis: YAxis =  _binding?.barChartView?.getAxisRight()!!
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false)
        val legend: Legend =  _binding?.barChartView?.getLegend()!!
        //setting the shape of the legend form to line, default square shape
        legend.form = Legend.LegendForm.LINE
        //setting the text size of the legend
        legend.textSize = 11f
        //setting the alignment of legend toward the chart
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        //setting the stacking direction of legend
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(false)
    }
}