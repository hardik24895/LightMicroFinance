package com.lightmicrofinance.commonproject.fragment


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lightmicrofinance.commonproject.databinding.FragmentChartBinding


class CollectionChartFragment : BaseFragment() {

    private var _binding: FragmentChartBinding? = null

    private val binding get() = _binding!!


    private lateinit var chart: CombinedChart
    private val count = 12

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chart = _binding?.barChartView!!
        initCHart()

    }

    fun initCHart() {
        chart.description.isEnabled = false
        chart.setBackgroundColor(Color.WHITE)
        chart.setDrawGridBackground(false)
        chart.setDrawBarShadow(false)
        chart.isHighlightFullBarEnabled = false

        // draw bars behind lines

        // draw bars behind lines
        chart.drawOrder = arrayOf(
            CombinedChart.DrawOrder.BAR,
            CombinedChart.DrawOrder.BUBBLE,
            CombinedChart.DrawOrder.CANDLE,
            CombinedChart.DrawOrder.LINE,
            CombinedChart.DrawOrder.SCATTER
        )

        val l = chart.legend
        l.isWordWrapEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)

        val rightAxis = chart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


        val leftAxis = chart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTH_SIDED
        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f

        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "Day"
            }
        }

        // xAxis.setValueFormatter(ValueFormatter { value, axis -> months.get(value.toInt() % months.length) })

        val data = CombinedData()

        data.setData(generateLineData())
        data.setData(generateBarData())
        // data.setData(generateBubbleData())
        // data.setData(generateScatterData())
        // data.setData(generateCandleData())
        //data.setValueTypeface(tfLight)

        xAxis.axisMaximum = data.xMax + 0.25f

        chart.data = data
        chart.invalidate()
    }

    private fun generateLineData(): LineData? {
        val d = LineData()
        val entries: ArrayList<Entry> = ArrayList()
        for (index in 0 until count) entries.add(
            Entry(
                index + 0.5f,
                getRandom(15f, 5f)
            )
        )
        val set = LineDataSet(entries, "Line DataSet")
        set.color = Color.rgb(240, 238, 70)
        set.lineWidth = 2.5f
        set.setCircleColor(Color.rgb(240, 238, 70))
        set.circleRadius = 5f
        set.fillColor = Color.rgb(240, 238, 70)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 10f
        set.valueTextColor = Color.rgb(240, 238, 70)
        set.axisDependency = YAxis.AxisDependency.LEFT
        d.addDataSet(set)
        return d
    }

    private fun generateBarData(): BarData? {
        val entries1: ArrayList<BarEntry> = ArrayList()
        //   val entries2: ArrayList<BarEntry> = ArrayList()
        for (index in 0 until count) {
            entries1.add(BarEntry(index.toFloat(), getRandom(25f, 25f)))

            // stacked
            // entries2.add(BarEntry(0f, floatArrayOf(getRandom(13f, 12f), getRandom(13f, 12f))))
        }
        val set1 = BarDataSet(entries1, "Bar 1")
        set1.color = Color.rgb(0, 143, 211)
        set1.valueTextColor = Color.rgb(0, 0, 0)
        set1.valueTextSize = 10f
        set1.axisDependency = YAxis.AxisDependency.LEFT
        //val set2 = BarDataSet(entries2, "")
//        set2.stackLabels = arrayOf("Stack 1", "Stack 2")
//        set2.setColors(Color.rgb(61, 165, 255), Color.rgb(23, 197, 255))
//        set2.valueTextColor = Color.rgb(61, 165, 255)
//        set2.valueTextSize = 10f
//        set2.axisDependency = YAxis.AxisDependency.LEFT
        val groupSpace = 0.06f
        val barSpace = 0.02f // x2 dataset
        val barWidth = 0.45f // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"
        val d = BarData(set1)
        // d.barWidth = barWidth

        // make this BarData object grouped
        //  d.groupBars(0f, groupSpace, barSpace) // start at x = 0
        return d
    }


    protected fun getRandom(range: Float, start: Float): Float {
        return (Math.random() * range).toFloat() + start
    }


}