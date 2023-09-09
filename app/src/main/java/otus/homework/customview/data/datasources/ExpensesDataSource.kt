package otus.homework.customview.data.datasources

import otus.homework.customview.R
import otus.homework.customview.data.models.ExpenseEntity

interface ExpensesDataSource {

    fun receiveExpenses(): List<ExpenseEntity>
}