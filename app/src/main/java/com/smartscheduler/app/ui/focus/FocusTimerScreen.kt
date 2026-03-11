package com.smartscheduler.app.ui.focus

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusTimerScreen(
    viewModel: FocusTimerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Focus Timer", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            
            Spacer(modifier = Modifier.weight(1f))

            // Timer Selection Row (Only visible when stopped)
            if (!state.isRunning && state.progress == 1f) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    TimePresetButton(25, viewModel)
                    TimePresetButton(45, viewModel)
                    TimePresetButton(60, viewModel)
                }
            } else {
                Spacer(modifier = Modifier.height(64.dp))
            }

            // Circular Timer
            Box(contentAlignment = Alignment.Center) {
                CircularTimerCanvas(progress = state.progress)
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val minutes = (state.timeRemainingMillis / 1000) / 60
                    val seconds = (state.timeRemainingMillis / 1000) % 60
                    Text(
                        text = String.format("%02d:%02d", minutes, seconds),
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (state.isRunning) "Focus mode active" else if (state.progress < 1f) "Paused" else "Ready to Focus",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Controls
            Row(
                modifier = Modifier.padding(bottom = 64.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                if (state.progress < 1f && !state.isRunning) {
                     // Stopped mid-way
                     FilledIconButton(
                         onClick = { viewModel.stopTimerEarly() },
                         modifier = Modifier.size(64.dp),
                         colors = IconButtonDefaults.filledIconButtonColors(
                             containerColor = MaterialTheme.colorScheme.errorContainer,
                             contentColor = MaterialTheme.colorScheme.onErrorContainer
                         )
                     ) {
                         Icon(Icons.Default.Stop, contentDescription = "Stop", modifier = Modifier.size(32.dp))
                     }
                }

                FilledIconButton(
                    onClick = { 
                        if (state.isRunning) viewModel.pauseTimer() 
                        else viewModel.startTimer() 
                    },
                    modifier = Modifier.size(80.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(
                        imageVector = if (state.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (state.isRunning) "Pause" else "Start",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TimePresetButton(minutes: Int, viewModel: FocusTimerViewModel) {
    OutlinedButton(
        onClick = { viewModel.setTimerMinutes(minutes) },
        shape = RoundedCornerShape(16.dp)
    ) {
        Text("$minutes min")
    }
}

@Composable
fun CircularTimerCanvas(progress: Float) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "timerProgress"
    )

    val trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
    val indicatorColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = Modifier
            .size(300.dp)
            .padding(16.dp)
            .shadow(16.dp, CircleShape, spotColor = indicatorColor.copy(alpha = 0.3f))
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        val strokeWidth = 16.dp.toPx()
        val diameterOffset = strokeWidth / 2
        val arcSize = size.width - strokeWidth
        
        // Background track
        drawCircle(
            color = trackColor,
            style = Stroke(width = strokeWidth)
        )

        // Progress arc
        drawArc(
            brush = Brush.sweepGradient(
                colors = listOf(
                    indicatorColor.copy(alpha = 0.6f),
                    indicatorColor
                )
            ),
            startAngle = -90f,
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            topLeft = Offset(diameterOffset, diameterOffset),
            size = Size(arcSize, arcSize),
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )
    }
}
