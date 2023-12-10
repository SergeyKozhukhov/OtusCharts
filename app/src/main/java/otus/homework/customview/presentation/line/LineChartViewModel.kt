package otus.homework.customview.presentation.line

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import otus.homework.customview.domain.models.Category
import otus.homework.customview.presentation.line.converters.LineDataConverter

class LineChartViewModel(
    private val converter: LineDataConverter = LineDataConverter()
) : ViewModel() {

    val uiState get() = _uiState.asStateFlow()
    private val _uiState: MutableStateFlow<LineChartUiState> = MutableStateFlow(LineChartUiState())

    fun load(categories: List<Category>) {
        val lineData = converter.convert(categories)
        _uiState.update { it.copy(current = lineData.firstOrNull(), lines = lineData) }
    }

    fun onDebugChanged(isChecked: Boolean) {
        _uiState.update { it.copy(isDebugEnabled = isChecked) }
    }

    companion object {

        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LineChartViewModel() as T
            }
        }
    }
}