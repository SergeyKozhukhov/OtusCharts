package otus.homework.customview.domain

import otus.homework.customview.domain.models.Category
import otus.homework.customview.domain.models.ChartByCategory

interface ExpensesInteractor {

    suspend fun getByCategory(): List<ChartByCategory>

    suspend fun receiveCategories(): List<Category>
}