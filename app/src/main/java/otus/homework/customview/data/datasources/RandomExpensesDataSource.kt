package otus.homework.customview.data.datasources

import otus.homework.customview.data.models.ExpenseEntity
import java.util.UUID
import kotlin.random.Random

class RandomExpensesDataSource : ExpensesDataSource {

    private val random = Random

    /*    override fun getRandomChart(nodeSize: Int):  {
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
    }*/
    override fun receiveExpenses(): List<ExpenseEntity> {
        val nodeSize = 10
        val list = mutableListOf<ExpenseEntity>()
        for (i in 0..nodeSize) {
            list.add(
                ExpenseEntity(
                    id = random.nextInt(),
                    name = UUID.randomUUID().toString().substring(0, 5),
                    amount = random.nextInt(-100, 100),
                    category = UUID.randomUUID().toString().substring(0, 10),
                    time = random.nextLong(1613419934, 1623419934)
                )
            )
        }
        return list
    }
}