package otus.homework.customview.data.converters

import otus.homework.customview.data.models.ExpenseEntity
import otus.homework.customview.domain.models.Category

class CategoriesConverter(private val expenseConverter: ExpenseConverter) {

    fun convert(source: List<ExpenseEntity>): List<Category> {
        return source.groupBy { it.category }.map { (categoryName, expenses) ->
            Category(
                name = categoryName,
                expenses = expenseConverter.convertList(expenses)
            )
        }
    }
}