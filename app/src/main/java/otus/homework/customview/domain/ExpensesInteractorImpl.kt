package otus.homework.customview.domain

import otus.homework.customview.domain.models.Category
import otus.homework.customview.domain.models.Expense

/**
 * Реализация интерактора данных по расходам
 *
 * @param repository репозиторий данных по расходам
 */
class ExpensesInteractorImpl(private val repository: ExpensesRepository) : ExpensesInteractor {

    override suspend fun getExpenses(max: Int?, force: Boolean): List<Expense> =
        repository.getExpenses(max, force)

    override suspend fun getCategories(maxExpenses: Int?, force: Boolean): List<Category> =
        getExpenses(maxExpenses, force).groupBy { it.category }.flatMap { (name, expenses) ->
            val sum = expenses.sumOf { it.amount.toLong() }
            listOf(Category(name = name, amount = sum, expenses = expenses))
        }
}