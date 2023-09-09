package otus.homework.customview.domain

import otus.homework.customview.domain.models.Category
import otus.homework.customview.domain.models.CategoryItem
import otus.homework.customview.domain.models.ChartByCategory

class ExpensesInteractorImpl(val repository: ExpensesRepository) : ExpensesInteractor {

    override suspend fun getByCategory(): List<ChartByCategory> {
        val items = mutableListOf<CategoryItem>()

        return emptyList()
    }

    override suspend fun receiveCategories(): List<Category> {
        return repository.receiveCategories()
    }
}