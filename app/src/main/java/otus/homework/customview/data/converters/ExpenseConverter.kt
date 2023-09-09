package otus.homework.customview.data.converters

import otus.homework.customview.data.models.ExpenseEntity
import otus.homework.customview.domain.models.Expense

class ExpenseConverter {

    fun convert(source: ExpenseEntity): Expense =
        Expense(
            id = source.id,
            name = source.name,
            amount = source.amount,
            time = source.time
        )

    fun convertList(source: List<ExpenseEntity>) = source.map { convert(it) }
}