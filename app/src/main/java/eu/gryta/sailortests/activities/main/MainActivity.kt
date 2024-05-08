package eu.gryta.sailortests.activities.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import eu.gryta.sailortests.md.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.actionBar?.hide()
        setContent {
            AppTheme {
                MainCompose()
            }
        }
    }
}