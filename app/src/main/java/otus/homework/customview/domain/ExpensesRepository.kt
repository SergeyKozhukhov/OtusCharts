package otus.homework.customview.domain

import com.example.charts.models.Chart
import com.example.transformer.DataFrame
import otus.homework.customview.domain.models.Category


interface ExpensesRepository {

    suspend fun getExpenses(): Chart

    suspend fun receiveCategories(): List<Category>

    fun receiveGeneratedCategories(nodeSize: Int): Chart

    fun receiveGeneratedCategories(): List<Category>

    suspend fun getDataFrame(): DataFrame
}