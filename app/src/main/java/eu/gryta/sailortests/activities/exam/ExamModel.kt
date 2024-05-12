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
import kotlin.random.Random
import kotlin.random.nextInt

class ExamModel(application: Application) : AndroidViewModel(application) {
    private val _application = getApplication<App>()
    private val _examState = MutableStateFlow(ExamState())

    val examState: StateFlow<ExamState> = _examState.asStateFlow()

    private fun reset() {
        _examState.value = ExamState()
    }

    init {
        reset()
    }

    private fun loadCategory(id: Int): List<ExamQuestion> {
        val inputStream: InputStream = _application.resources.openRawResource(id)
        val reader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
        val csv = CSVReader(reader).readAll()

        val indexes = generateSequence {
            // this lambda is the source of the sequence's values
            Random.nextInt(1..<csv.size)
        }
            .distinct()
            .take(min(15, csv.size - 1))
            .sorted()
            .toSet()
        val selected = csv.filterIndexed { index, _ -> index in indexes }

        val questions = selected.map {
            ExamQuestion(
                question = it[0],

                answer1 = when (it[1] != "") {
                    true -> it[1]
                    false -> null
                },
                points1 = when (it[2] != "") {
                    true -> it[2].toFloat()
                    false -> null
                },

                answer2 = when (it[3] != "") {
                    true -> it[3]
                    false -> null
                },
                points2 = when (it[4] != "") {
                    true -> it[4].toFloat()
                    false -> null
                },

                answer3 = when (it[5] != "") {
                    true -> it[5]
                    false -> null
                },
                points3 = when (it[6] != "") {
                    true -> it[6].toFloat()
                    false -> null
                },

                answer4 = when (it[7] != "") {
                    true -> it[7]
                    false -> null
                },
                points4 = when (it[8] != "") {
                    true -> it[8].toFloat()
                    false -> null
                },

                )
        }
        return questions
    }

    fun generateExam() {
        val categories: MutableList<ExamCategory> = mutableListOf()
        categories.add(
            ExamCategory(
                name = _application.resources.getString(R.string.navigation_rules),
                questions = loadCategory(id = R.raw.locja),
            )
        )
        categories.add(
            ExamCategory(
                name = _application.resources.getString(R.string.sailing_law),
                questions = loadCategory(id = R.raw.prawo),
            )
        )
        val newState = _examState.value.copy(
            state = 1,
            exam = Exam(
                categories = categories,
            )
        )
        _examState.value = newState
    }

    fun selectOption(cId: Int, qId: Int, optionId: Int) {
        val question = _examState.value.exam!!.categories[cId].questions[qId]

        val newQuestionState = when (optionId) {
            1 -> {
                var nqs = question.copy(
                    selected1 = true
                )
                if (!question.multiChoice()) {
                    nqs = nqs.copy(
                        selected2 = false,
                        selected3 = false,
                        selected4 = false
                    )
                }
                nqs
            }

            2 -> {
                var nqs = question.copy(
                    selected2 = true
                )
                if (!question.multiChoice()) {
                    nqs = nqs.copy(
                        selected1 = false,
                        selected3 = false,
                        selected4 = false
                    )
                }
                nqs
            }

            3 -> {
                var nqs = question.copy(
                    selected3 = true
                )
                if (!question.multiChoice()) {
                    nqs = nqs.copy(
                        selected1 = false,
                        selected2 = false,
                        selected4 = false
                    )
                }
                nqs
            }

            4 -> {
                var nqs = question.copy(
                    selected4 = true
                )
                if (!question.multiChoice()) {
                    nqs = nqs.copy(
                        selected1 = false,
                        selected2 = false,
                        selected3 = false
                    )
                }
                nqs
            }

            else -> question.copy()
        }

        val newCategories = _examState.value.exam!!.categories.toMutableList()
        val newQuestions = newCategories[cId].questions.toMutableList()

        newQuestions[qId] = newQuestionState

        newCategories[cId] = newCategories[cId].copy(
            questions = newQuestions
        )

        _examState.value = _examState.value.copy(
            exam = _examState.value.exam!!.copy(
                categories = newCategories
            )
        )
    }
}