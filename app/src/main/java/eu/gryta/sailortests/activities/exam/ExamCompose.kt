package eu.gryta.sailortests.activities.exam

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


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
fun ExamCompose(uiViewModel: ExamModel = viewModel()) {

    val examState by uiViewModel.examState.collectAsStateWithLifecycle()
    val navController: NavHostController = rememberNavController()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxSize()
    )
    {
        if (examState.state == 0) {
            Button(onClick = { uiViewModel.generateExam() }) {
                Text("Generate")
            }
        }
        else {
            NavHost(
                navController = navController,
                startDestination = "question/0",
            ) {
                composable(
                    "question/{questionId}",
                    arguments = listOf(navArgument("questionId") { type = NavType.IntType })
                ) {
                    val qId: Int = it.arguments?.getInt("questionId")!!
                    QuestionCard(question = examState.exam!!.categories[0].questions[qId])
                    Button(onClick = {
                        navController.navigate("question/${qId+1}")
                    }){
                        Text(text = "Next")
                    }
                }
            }
        }
    }
}