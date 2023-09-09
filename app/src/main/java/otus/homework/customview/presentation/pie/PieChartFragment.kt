package otus.homework.customview.presentation.pie

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.charts.pie.PieChart
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.launch
import otus.homework.customview.R
import otus.homework.customview.data.ExpensesRepositoryImpl
import otus.homework.customview.data.converters.CategoriesConverter
import otus.homework.customview.data.converters.DataFrameConverter
import otus.homework.customview.data.converters.ExpenseConverter
import otus.homework.customview.data.converters.LegacyExpenseConverter
import otus.homework.customview.data.datasources.LocalExpensesDataSource
import otus.homework.customview.data.datasources.RandomExpensesDataSource
import otus.homework.customview.presentation.ChartViewModel

class PieChartFragment : Fragment() {

    private lateinit var chartViewModel: ChartViewModel
    private lateinit var pieChartViewModel: PieChartViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val repository = ExpensesRepositoryImpl(
            LocalExpensesDataSource(requireContext().applicationContext, ObjectMapper()),
            RandomExpensesDataSource(),
            CategoriesConverter(ExpenseConverter()),
            LegacyExpenseConverter(),
            DataFrameConverter()
        )
        chartViewModel = ViewModelProvider(
            requireActivity(),
            ChartViewModel.provideFactory(repository)
        )[ChartViewModel::class.java]
        pieChartViewModel =
            ViewModelProvider(this, PieChartViewModel.provide())[PieChartViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pie_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pieChart: PieChart = view.findViewById(R.id.pie_chart)
        /*        chartViewModel.liveData.observe(this.viewLifecycleOwner) { chart ->
                    pieChart.updateNodes(chart)
                }*/
        chartViewModel.dataFrameLiveData.observe(this.viewLifecycleOwner) { dataFrame ->
            pieChart.updateNodes(dataFrame)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                pieChartViewModel.styleState.collect { pieChart.setStyle(it) }
            }
        }

        val button: Button = view.findViewById(R.id.style_button)
        button.setOnClickListener { pieChartViewModel.changeStyle() }
    }


    companion object {
        fun newInstance() = PieChartFragment()
    }
}