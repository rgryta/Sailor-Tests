package eu.gryta.sailortests.activities.exam

data class ExamGenerateState(

    val fabEnabled: Boolean = true,

    val locjaCount: Int = 0,
    val prawoCount: Int = 0,

    val locjaMaxCount: Int = 0,
    val prawoMaxCount: Int = 0,
)