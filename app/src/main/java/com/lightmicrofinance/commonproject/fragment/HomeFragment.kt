package com.lightmicrofinance.commonproject.fragment


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commonProject.extention.showAlert
import com.commonProject.network.CallbackObserver
import com.commonProject.network.Networking
import com.commonProject.network.addTo
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.lightmicrofinance.commonproject.databinding.FragmentHomeBinding
import com.lightmicrofinance.commonproject.modal.TargetModal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.xml.datatype.DatatypeConstants.DAYS
import javax.xml.datatype.DatatypeConstants.MONTHS


class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var  chart: CombinedChart
    private val count = 12

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
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chart = _binding?.barChartView!!
       // getTargetData()
        // initBarChart()
        //showBarChart()
        initCHart()

    }

    fun  initCHart(){
        chart.description.isEnabled = false
        chart.setBackgroundColor(Color.WHITE)
        chart.setDrawGridBackground(false)
        chart.setDrawBarShadow(false)
        chart.isHighlightFullBarEnabled = false

        // draw bars behind lines

        // draw bars behind lines
        chart.drawOrder = arrayOf(
            DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
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
        xAxis.position = XAxisPosition.BOTH_SIDED
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

 /*   private fun showBarChart() {
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
        val xAxis: XAxis = _binding?.barChartView?.getXAxis()!!
        //change the position of x-axis to the bottom
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //set the horizontal distance of the grid line
        xAxis.granularity = 1f
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false)
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false)
        val leftAxis: YAxis = _binding?.barChartView?.getAxisLeft()!!
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false)
        val rightAxis: YAxis = _binding?.barChartView?.getAxisRight()!!
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false)
        val legend: Legend = _binding?.barChartView?.getLegend()!!
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
    }*/

    fun getTargetData() {

        val params = HashMap<String, Any>()
        params["FECode"] = session.user.data?.fECode.toString()

        Networking
            .with(requireContext())
            .getServices()
            .getTarget(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<TargetModal>() {
                override fun onSuccess(response: TargetModal) {
                    val data = response.data
                    if (response.error == false) {
                        _binding?.txtTargetAmount!!.text = "\u20b9 " + data?.target
                        _binding?.txtTotalCollectionAmount!!.text = "\u20b9 " + data?.collected
                        _binding?.txtCollectionPersentageAmount!!.text = data?.percentage
                    } else {
                        showAlert(response.message.toString())
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    showAlert(message)
                    hideProgressbar()
                }

            }).addTo(autoDisposable)
    }
}