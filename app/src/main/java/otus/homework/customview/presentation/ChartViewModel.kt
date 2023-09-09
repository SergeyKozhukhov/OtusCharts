package otus.homework.customview.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.charts.models.Chart
import com.example.charts.models.DataFrameLegacy
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import otus.homework.customview.domain.ExpensesRepository
import otus.homework.customview.domain.models.Category

class ChartViewModel(
    private val repository: ExpensesRepository
) : ViewModel() {

    val liveData = MutableLiveData<Chart>()
    val dataFrameLiveData = MutableLiveData<DataFrameLegacy>()

    private val handler = CoroutineExceptionHandler { _, _ ->

    }

    fun getCategories() = viewModelScope.launch(handler) {
        val categories = repository.receiveCategories()
        dataFrameLiveData.value = convert(categories)
    }

    private fun convert(categories: List<Category>): DataFrameLegacy {
        val values1 = mutableListOf<Any>()
        val values2 = mutableListOf<Any>()
        val values3 = mutableListOf<Any>()

        categories.forEach {
            values2.add(it.name)
            values1.add(it.expenses.sumOf { it.amount })
            values3.add(it.expenses.sumOf { it.time })
        }

        val field1 = DataFrameLegacy.FieldLegacy(
            "amount",
            "int",
            values1
        )

        val field2 = DataFrameLegacy.FieldLegacy(
            "category",
            "string",
            values2
        )

        val field3 = DataFrameLegacy.FieldLegacy(
            "time",
            "long",
            values3
        )

        val dataFrame = DataFrameLegacy(listOf(field1, field2, field3))
        return dataFrame
    }

    fun getData() = viewModelScope.launch(handler) {
        val items = repository.getExpenses()
        liveData.value = items
        dataFrameLiveData.value = convert(chart = items)
    }

    fun getRandom() {
        val items = repository.receiveGeneratedCategories()
        //liveData.value = items
        dataFrameLiveData.value = convert(items)
    }

    private fun convert(chart: Chart): DataFrameLegacy {
        val values1 = mutableListOf<Any>()
        val values2 = mutableListOf<Any>()
        chart.nodes.map {
            values1.add(it.amount)
            values2.add(it.category)
        }

        val field1 = DataFrameLegacy.FieldLegacy(
            "amount",
            "int",
            values1
        )

        val field2 = DataFrameLegacy.FieldLegacy(
            "category",
            "string",
            values2
        )
        val dataFrame = DataFrameLegacy(listOf(field1, field2))
        return dataFrame
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun provideFactory(repository: ExpensesRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                ChartViewModel(repository) as T
        }
    }
}