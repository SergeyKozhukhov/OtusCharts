package otus.homework.customview.data.converters

import com.example.transformer.DataFrame
import com.example.transformer.Field
import otus.homework.customview.data.models.ExpenseEntity

class DataFrameConverter {

    fun convert(source: List<ExpenseEntity>): DataFrame {
        val idValues = mutableListOf<Int>()
        val nameValues = mutableListOf<String>()
        val amountValues = mutableListOf<Int>()
        val categoryValues = mutableListOf<String>()
        val timeValues = mutableListOf<Long>()
        source.forEach { expense ->
            idValues.add(expense.id)
            nameValues.add(expense.name)
            amountValues.add(expense.amount)
            categoryValues.add(expense.category)
            timeValues.add(expense.time)
        }
        val idField = Field("id", idValues)
        val nameField = Field("id", nameValues)
        val amountField = Field("id", amountValues)
        val categoryField = Field("id", categoryValues)
        val timeField = Field("id", timeValues)

        val dataFrame = DataFrame(
            series = listOf(
                idField,
                nameField,
                amountField,
                categoryField,
                timeField
            )
        )
        return dataFrame
    }
}