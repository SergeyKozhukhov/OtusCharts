package otus.homework.customview.domain.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Expense(
    val id: Int,
    val name: String,
    val amount: Int,
    val time: Long
)
