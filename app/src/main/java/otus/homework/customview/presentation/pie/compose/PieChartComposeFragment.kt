/*
package otus.homework.customview.presentation.pie.compose

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.charts.pie.PieChart
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.launch
import otus.homework.customview.R
import otus.homework.customview.data.datasources.ChartDataSource
import otus.homework.customview.data.RepositoryImpl
import otus.homework.customview.data.converters.ChartConverter
import otus.homework.customview.presentation.ChartViewModel
import otus.homework.customview.presentation.pie.PieChartViewModel

class PieChartComposeFragment : Fragment() {

    private lateinit var chartViewModel: ChartViewModel
    private lateinit var pieChartViewModel: PieChartViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val repository = RepositoryImpl(
            ChartDataSource(requireContext().applicationContext, ObjectMapper()),
            ChartConverter()
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
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            // In Compose world
            MaterialTheme {
                Text("Hello Compose!")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pieChart: PieChart = view.findViewById(R.id.pie_chart)
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
        fun newInstance() = PieChartComposeFragment()
    }
}*/
