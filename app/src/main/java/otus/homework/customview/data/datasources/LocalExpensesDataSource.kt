package otus.homework.customview.data.datasources

import android.content.Context
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import otus.homework.customview.R
import otus.homework.customview.data.models.ExpenseEntity
import java.io.File

class LocalExpensesDataSource(
    private val context: Context,
    private val objectMapper: ObjectMapper
) : ExpensesDataSource {

    private val file = File(context.packageResourcePath, FILE_NAME)
    private val typeReference = object : TypeReference<List<ExpenseEntity>>() {}

    override fun receiveExpenses(): List<ExpenseEntity> =
        objectMapper.readValue(context.resources.openRawResource(R.raw.payload), typeReference)

    private companion object {
        const val FILE_NAME = "raw/payload.json"
    }
}