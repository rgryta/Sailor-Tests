package eu.gryta.sailortests.activities.exam

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chargemap.compose.numberpicker.NumberPicker
import eu.gryta.sailortests.R
import eu.gryta.sailortests.ui.findActivity
import kotlinx.coroutines.delay

sealed class BackPress {
    data object Idle : BackPress()
    data object InitialTouch : BackPress()
}


@Composable
fun QuestionCard(question: ExamQuestion){

    Box(
        modifier= Modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(10.dp)
    ) {
        Text(question.question)
    }
}

@Composable
fun ExamSetting(
    examGenerateModel: ExamGenerateModel,
    shape: Shape,

    settingNameId: Int,
    count: Int,
    maxCount: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(5.dp)
            .clip(shape)
            .clip(shape = RoundedCornerShape(20.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        val setting = LocalContext.current.resources.getString(settingNameId)
        Text(setting, color = MaterialTheme.colorScheme.onPrimaryContainer)
        NumberPicker(
            value = count,
            onValueChange = {
                examGenerateModel.setCategoryCount(settingNameId, count = it)
            },
            range = 0..maxCount,
            dividersColor = MaterialTheme.colorScheme.onPrimaryContainer,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)
        )
    }
}

private object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}

@Composable
fun GenerateExam(
    navController: NavHostController,
    examModel: ExamModel,
    examGenerateModel: ExamGenerateModel = viewModel()
) {
    val examGenerateState = examGenerateModel.examGenerateState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            CompositionLocalProvider(
                LocalRippleTheme provides
                        if (examGenerateState.value.fabEnabled) LocalRippleTheme.current else NoRippleTheme
            ) {
                FloatingActionButton(
                    containerColor = if (examGenerateState.value.fabEnabled) MaterialTheme.colorScheme.primaryContainer else Color.DarkGray,
                    onClick = {
                        if (examGenerateState.value.fabEnabled) {
                            examModel.generateExam()
                            navController.navigate("c/0/init") {
                                popUpTo(0)
                            }
                        }
                    },
                ) {
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = context.resources.getString(R.string.start_exam),
                        tint = if (examGenerateState.value.fabEnabled)
                            LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                        else Color.LightGray
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            val cornerRadius: Dp = 28.dp
            val topShape = RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius)
            val bottomShape =
                RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius)

            val settings = listOf(
                mapOf(
                    "setting" to R.string.navigation_rules,
                    "count" to examGenerateState.value.locjaCount,
                    "maxCount" to examGenerateState.value.locjaMaxCount
                ), mapOf(
                    "setting" to R.string.sailing_law,
                    "count" to examGenerateState.value.prawoCount,
                    "maxCount" to examGenerateState.value.prawoMaxCount
                )
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
            ) {
                var shape: Shape
                items(settings.size) { index ->
                    shape = when (index) {
                        0 -> topShape
                        settings.size - 1 -> bottomShape
                        else -> RectangleShape
                    }
                    ExamSetting(
                        examGenerateModel = examGenerateModel,
                        shape = shape,

                        settingNameId = settings[index]["setting"]!!,
                        count = settings[index]["count"]!!,
                        maxCount = settings[index]["maxCount"]!!,
                    )
                }
            }
        }
    }
}


@Composable
fun ExamCategory(cId: Int, examState: ExamState, navController: NavHostController) {
    Text(examState.exam!!.categories[cId].name)
    Button(onClick = {
        navController.navigate("c/$cId/q/0")
    }) {
        Text(text = "Next")
    }
}

@Composable
fun ExamCompose(examModel: ExamModel = viewModel()) {

    val examState by examModel.examState.collectAsStateWithLifecycle()
    val navController: NavHostController = rememberNavController()

    var showToast by remember { mutableStateOf(false) }
    var backPressState by remember { mutableStateOf<BackPress>(BackPress.Idle) }
    val context = LocalContext.current

    if (showToast) {
        Toast.makeText(
            context,
            context.resources.getString(R.string.double_exit),
            Toast.LENGTH_SHORT
        ).show()
        showToast = false
    }
    LaunchedEffect(key1 = backPressState) {
        if (backPressState == BackPress.InitialTouch) {
            delay(2000)
            backPressState = BackPress.Idle
        }
    }

    if (examState.state > 0) {
        BackHandler(enabled = true) {
            if (backPressState != BackPress.Idle) {
                context.findActivity()!!.finish()
            } else {
                backPressState = BackPress.InitialTouch
                showToast = true
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxSize()
    )
    {
        Text("Backstack size: ${navController.currentBackStack.value.size}")
        NavHost(
            navController = navController,
            startDestination = "start",
            ) {
            composable("start") {
                GenerateExam(navController = navController, examModel = examModel)
            }
            composable(
                "c/{categoryId}/init",
                arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
            ) {
                val cId: Int = it.arguments?.getInt("categoryId")!!
                ExamCategory(cId = cId, examState = examState, navController = navController)
            }
                composable(
                    "c/{categoryId}/q/{questionId}",
                    arguments = listOf(
                        navArgument("categoryId") { type = NavType.IntType },
                        navArgument("questionId") { type = NavType.IntType })
                ) {
                    val cId: Int = it.arguments?.getInt("categoryId")!!
                    val qId: Int = it.arguments?.getInt("questionId")!!

                    val maxCId: Int = examState.exam!!.categories.size
                    val maxQId: Int = examState.exam!!.categories[cId].questions.size

                    QuestionCard(question = examState.exam!!.categories[cId].questions[qId])
                    Button(onClick = {
                        if (qId + 1 == maxQId) {
                            if (cId + 1 == maxCId) {
                                navController.navigate("finish")
                            } else {
                                navController.navigate("c/${cId + 1}/init")
                            }
                        } else {
                            navController.navigate("c/$cId/q/${qId + 1}")
                        }
                    }){
                        Text(text = "Next")
                    }
                }
            composable("finish") {
                Text("Finish")
            }
            }

    }
}