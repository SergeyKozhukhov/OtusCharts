package otus.homework.customview.presentation.pie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.charts.pie.PieStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PieChartViewModel : ViewModel() {

    val styleState get() = _styleState.asStateFlow()
    private val _styleState = MutableStateFlow(PieStyle.PIE)

    fun changeStyle() {
        _styleState.value = if (_styleState.value == PieStyle.PIE) PieStyle.DONUT else PieStyle.PIE
    }


    companion object {

        fun provide() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                PieChartViewModel() as T
        }
    }
}