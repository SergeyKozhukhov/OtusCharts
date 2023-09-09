package otus.homework.customview

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.lifecycle.ViewModelProvider
import com.fasterxml.jackson.databind.ObjectMapper
import otus.homework.customview.data.datasources.LocalExpensesDataSource
import otus.homework.customview.data.ExpensesRepositoryImpl
import otus.homework.customview.data.converters.CategoriesConverter
import otus.homework.customview.data.converters.DataFrameConverter
import otus.homework.customview.data.converters.ExpenseConverter
import otus.homework.customview.data.converters.LegacyExpenseConverter
import otus.homework.customview.data.datasources.RandomExpensesDataSource
import otus.homework.customview.domain.ExpensesRepository
import otus.homework.customview.presentation.ChartViewModel
import otus.homework.customview.presentation.line.LineChartFragment
import otus.homework.customview.presentation.pie.PieChartFragment

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var repository: ExpensesRepository
    private lateinit var viewModel: ChartViewModel

    private lateinit var container: FragmentContainerView
    private lateinit var pieChartButton: Button
    private lateinit var lineChartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        repository = ExpensesRepositoryImpl(
            LocalExpensesDataSource(this.applicationContext, ObjectMapper()),
            RandomExpensesDataSource(),
            CategoriesConverter(ExpenseConverter()),
            LegacyExpenseConverter(),
            DataFrameConverter()
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(
            this,
            ChartViewModel.provideFactory(repository)
        )[ChartViewModel::class.java]

        pieChartButton = findViewById(R.id.pie_chart_button)
        lineChartButton = findViewById(R.id.line_chart_button)
        val randomButton: Button = findViewById(R.id.random_button)
        container = findViewById(R.id.chart_container)

        pieChartButton.setOnClickListener { openPieChart() }
        // findViewById<Button>(R.id.pie_chart_compose_button).setOnClickListener { openPieComposeChart() }
        lineChartButton.setOnClickListener { openLineChart() }
        randomButton.setOnClickListener { viewModel.getRandom() }

        if (savedInstanceState == null) {
            viewModel.getCategories()
        }
    }

    override fun openPieChart() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.chart_container, PieChartFragment.newInstance())
            .setTransition(TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun openPieComposeChart() {
        /*        supportFragmentManager.beginTransaction()
                    .replace(R.id.chart_container, PieChartComposeFragment.newInstance())
                    .setTransition(TRANSIT_FRAGMENT_OPEN)
                    .commit()*/
    }


    override fun openLineChart() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.chart_container, LineChartFragment.newInstance())
            .setTransition(TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}