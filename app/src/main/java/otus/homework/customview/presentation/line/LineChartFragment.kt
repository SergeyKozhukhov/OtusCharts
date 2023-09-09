package otus.homework.customview.presentation.line

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.charts.line.LineChart
import com.fasterxml.jackson.databind.ObjectMapper
import otus.homework.customview.R
import otus.homework.customview.data.datasources.LocalExpensesDataSource
import otus.homework.customview.data.ExpensesRepositoryImpl
import otus.homework.customview.data.converters.CategoriesConverter
import otus.homework.customview.data.converters.DataFrameConverter
import otus.homework.customview.data.converters.ExpenseConverter
import otus.homework.customview.data.converters.LegacyExpenseConverter
import otus.homework.customview.data.datasources.RandomExpensesDataSource
import otus.homework.customview.presentation.ChartViewModel

class LineChartFragment : Fragment() {

    private lateinit var viewModel: ChartViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val repository = ExpensesRepositoryImpl(
            LocalExpensesDataSource(requireContext().applicationContext, ObjectMapper()),
            RandomExpensesDataSource(),
            CategoriesConverter(ExpenseConverter()),
            LegacyExpenseConverter(),
            DataFrameConverter()
        )
        viewModel = ViewModelProvider(
            requireActivity(),
            ChartViewModel.provideFactory(repository)
        )[ChartViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_line_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lineChart: LineChart = view.findViewById(R.id.line_chart)
        val debugCheckBox: CheckBox = view.findViewById(R.id.debug_checkbox)
        viewModel.dataFrameLiveData.observe(this.viewLifecycleOwner) {
            lineChart.updateNodes(dataFrame = it)

        }
/*        viewModel.liveData.observe(this.viewLifecycleOwner) { chart ->
            // lineChart.updateNodes(chart)
        }*/
        debugCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            lineChart.setDebugMode(isChecked)
        }
    }


    companion object {
        fun newInstance() = LineChartFragment()
    }
}