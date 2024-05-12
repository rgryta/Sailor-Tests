package eu.gryta.sailortests.activities.exam

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.opencsv.CSVReader
import eu.gryta.sailortests.App
import eu.gryta.sailortests.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import kotlin.math.min

class ExamGenerateModel(application: Application) : AndroidViewModel(application) {
    private val _application = getApplication<App>()
    private val _examGenerateState = MutableStateFlow(ExamGenerateState())

    val examGenerateState: StateFlow<ExamGenerateState> = _examGenerateState.asStateFlow()

    private fun reset() {
        val locjaMaxCount = categoryCount(id = R.raw.locja)
        val prawoMaxCount = categoryCount(id = R.raw.prawo)

        _examGenerateState.value = ExamGenerateState(
            locjaCount = min(15, locjaMaxCount),
            locjaMaxCount = locjaMaxCount,

            prawoCount = min(15, prawoMaxCount),
            prawoMaxCount = prawoMaxCount,

            )
    }

    init {
        reset()
    }

    fun setCategoryCount(id: Int, count: Int) {
        val newExamGenerateState = when (id) {
            R.string.navigation_rules -> _examGenerateState.value.copy(
                locjaCount = count,
                fabEnabled = if (count > 0) true else (_examGenerateState.value.prawoCount > 0)
            )

            R.string.sailing_law -> _examGenerateState.value.copy(
                prawoCount = count,
                fabEnabled = if (count > 0) true else (_examGenerateState.value.locjaCount > 0)
            )

            else -> _examGenerateState.value
        }

        _examGenerateState.value = newExamGenerateState
    }

    private fun categoryCount(id: Int): Int {
        val inputStream: InputStream = _application.resources.openRawResource(id)
        val reader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
        val csv = CSVReader(reader).readAll()

        return csv.size - 1
    }

}