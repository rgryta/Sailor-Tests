package eu.gryta.sailortests.activities

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eu.gryta.sailortests.R

@Composable
fun MainCompose() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxSize()
    )
    {
        Image(
            painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(0.5f)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth(0.5f),
        ) {

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    Log.d("BUT", "Clicked on 1st button")
                }
            ) {
                Text(text = "Start")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    Log.d("BUT", "Clicked on 1st button")
                }
            ) {
                Text(text = "History")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    Log.d("BUT", "Clicked on 1st button")
                }
            ) {
                Text(text = "Settings")
            }

        }

        Spacer(modifier = Modifier.fillMaxWidth())
    }
}