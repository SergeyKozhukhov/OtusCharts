package com.example.charts.models

import java.util.Objects

data class DataFrameLegacy(
    val fields: List<FieldLegacy>
) {
    data class FieldLegacy(
        val name: String,
        val type: String,
        val values: List<Any>
    )
}


data class DataFrame(
    val fields: List<Field<Any>>
)

data class Field<T>(
    val name: String,
    val values: List<T>,
    val metadata: Any? = null
)