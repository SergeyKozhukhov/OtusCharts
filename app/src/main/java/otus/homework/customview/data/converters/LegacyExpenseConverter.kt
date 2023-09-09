package otus.homework.customview.data.converters

import com.example.charts.models.Chart
import com.example.charts.models.Node
import otus.homework.customview.data.models.ExpenseEntity

class LegacyExpenseConverter {

    fun convert(list: List<ExpenseEntity>): Chart {
        return Chart(
            nodes = list.map { elementData ->
                Node(
                    id = elementData.id,
                    name = elementData.name,
                    amount = elementData.amount,
                    category = elementData.category,
                    time = elementData.time
                )
            }
        )
    }
}