package eu.gryta.sailortests.activities.exam

import kotlin.math.max


data class ExamQuestion(
    val question: String, // Question content

    val answer1: String? = null,
    val points1: Float? = null,
    val selected1: Boolean = false,

    val answer2: String? = null,
    val points2: Float? = null,
    val selected2: Boolean = false,

    val answer3: String? = null,
    val points3: Float? = null,
    val selected3: Boolean = false,

    val answer4: String? = null,
    val points4: Float? = null,
    val selected4: Boolean = false,
) {
    fun maxPoints(): Float {
        var p = 0f
        if (points1 != null) {
            p += max(points1, 0f)
        }
        if (points2 != null) {
            p += max(points2, 0f)
        }
        if (points3 != null) {
            p += max(points3, 0f)
        }
        if (points4 != null) {
            p += max(points4, 0f)
        }
        return p
    }

    fun points(): Float {
        val p1 = when (selected1) {
            true -> points1!!
            else -> 0f
        }
        val p2 = when (selected2) {
            true -> points2!!
            else -> 0f
        }
        val p3 = when (selected3) {
            true -> points3!!
            else -> 0f
        }
        val p4 = when (selected4) {
            true -> points4!!
            else -> 0f
        }
        return p1 + p2 + p3 + p4
    }

    fun multiChoice(): Boolean {
        val points: List<Float?> = mutableListOf(points1, points2, points3, points4)
        val positivePoints = points.filter { (it != null) && (it > 0) }
        return positivePoints.size > 1
    }
}

data class ExamCategory(
    val name: String,
    val questions: List<ExamQuestion>,
) {
    fun maxPoints(): Float {
        return questions.map { it.maxPoints() }.sum()
    }

    fun points(): Float {
        return questions.map { it.points() }.sum()
    }
}

data class Exam(
    val categories: List<ExamCategory>,
) {
    fun maxPoints(): Float {
        return categories.map { it.maxPoints() }.sum()
    }

    fun points(): Float {
        return categories.map { it.points() }.sum()
    }
}

data class ExamState(
    /*
    States:
     - 0: Loading
     - 1: Loaded and waiting for start
     - 2: Exam started
     - 3: Exam finished
    */

    val state: Int = 0,

    val exam: Exam? = null,
)