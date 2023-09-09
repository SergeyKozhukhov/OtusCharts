package otus.homework.customview.data

import com.example.charts.models.Chart
import com.example.charts.models.Node
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import otus.homework.customview.data.converters.CategoriesConverter
import otus.homework.customview.data.converters.DataFrameConverter
import otus.homework.customview.data.converters.LegacyExpenseConverter
import otus.homework.customview.data.datasources.ExpensesDataSource
import otus.homework.customview.domain.ExpensesRepository
import otus.homework.customview.domain.models.Category
import java.util.UUID
import kotlin.random.Random

class ExpensesRepositoryImpl(
    private val localDataSource: ExpensesDataSource,
    private val randomDataSource: ExpensesDataSource,
    private val categoriesConverter: CategoriesConverter,
    private val expenseConverter: LegacyExpenseConverter,
    private val dataFrameConverter: DataFrameConverter
) : ExpensesRepository {

    private val random = Random

    override suspend fun receiveCategories() = withContext(Dispatchers.IO) {
        val expenses = localDataSource.receiveExpenses()
        categoriesConverter.convert(expenses)
    }

    override fun receiveGeneratedCategories(): List<Category> {
        return categoriesConverter.convert(randomDataSource.receiveExpenses())
    }

    override suspend fun getDataFrame() = withContext(Dispatchers.IO) {
        val expenses = localDataSource.receiveExpenses()
        dataFrameConverter.convert(expenses)
    }

    override suspend fun getExpenses(): Chart = withContext(Dispatchers.IO) {
        // TODO: runInterruptible {  }
        val elements = localDataSource.receiveExpenses()
        expenseConverter.convert(elements)
    }

    override fun receiveGeneratedCategories(nodeSize: Int): Chart {
        val list = mutableListOf<Node>()
        for (i in 0..nodeSize) {
            val node = Node(
                id = random.nextInt(),
                name = UUID.randomUUID().toString().substring(0, 5),
                amount = random.nextInt(100),
                category = UUID.randomUUID().toString().substring(0, 10),
                time = random.nextLong(1613419934, 1623419934)
            )
            list.add(node)
        }
        return Chart(list)
    }
}