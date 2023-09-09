package otus.homework.customview.domain.models

data class ChartByCategory(
    val category: String,
    val items: List<CategoryItem>
)

data class CategoryItem(
    val id: Int,
    val name: String,
    val amount: Int,
    val time: Long
)