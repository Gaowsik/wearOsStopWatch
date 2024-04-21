package com.example.samplewatchapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import com.example.samplewatchapp.R
import com.example.samplewatchapp.presentation.theme.SampleWatchAppTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<StopWatchViewModel>()
            val timeState by viewModel.timerState.collectAsStateWithLifecycle()
            val stopWatchText by viewModel.stopWatchText.collectAsStateWithLifecycle()

            Scaffold(timeText = { TimeText(timeTextStyle = TimeTextDefaults.timeTextStyle(fontSize = 10.sp)) }) {
                stopWatchUI(
                    timeState,
                    stopWatchText,
                    onToggleRunning = { viewModel.toggleRunning() }) {
                    viewModel.resetTimer()
                }
            }

        }


    }
}

@Composable
private fun stopWatchUI(
    timeState: TimeState,
    stopWatchText: String, onToggleRunning: () -> Unit, onResetClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = stopWatchText, fontSize = 20.sp, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick =  onToggleRunning ) {
                //   Icon(imageVector = if (timeState = TimeState.RUNNING) :Icons.D , contentDescription ="")
                Icon(
                    imageVector = if (timeState == TimeState.RUNNING) {
                        Icons.Default.Pause
                    } else {
                        Icons.Default.PlayArrow
                    }, contentDescription = null
                )

            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onResetClick,
                enabled = timeState != TimeState.RESET,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)
            ) {
                //   Icon(imageVector = if (timeState = TimeState.RUNNING) :Icons.D , contentDescription ="")
                Icon(imageVector = Icons.Default.Stop, contentDescription = null)

            }
        }


    }


}




