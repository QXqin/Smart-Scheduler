package com.smartscheduler.app.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartscheduler.app.domain.model.TimelineEvent
import com.smartscheduler.app.domain.model.Todo
import com.smartscheduler.app.ui.theme.FixedEventColor
import com.smartscheduler.app.ui.theme.ScheduledEventColor
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToStatistics: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    var currentDate by remember { mutableStateOf(LocalDate.now()) }

    // Live clock update
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now()
            currentDate = LocalDate.now()
            delay(1000)
        }
    }

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val dateFormatter = DateTimeFormatter.ofPattern("M月d日 EEEE", Locale.getDefault())

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header / Clock
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                // Top Right Action Button for Statistics
                IconButton(
                    onClick = onNavigateToStatistics,
                    modifier = Modifier.align(Alignment.TopEnd).offset(x = 16.dp, y = (-16).dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = "数据统计",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = currentTime.format(timeFormatter),
                        style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = currentDate.format(dateFormatter),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            // Upcoming Schedule & Todos overview
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item {
                    OverviewSectionHead(icon = Icons.Default.Schedule, title = "今日待办日程")
                }

                if (state.todayEvents.isEmpty()) {
                    item {
                        EmptyStateCard("今天还没有被安排日程，试着去生成一个吧！")
                    }
                } else {
                    items(state.todayEvents) { event ->
                        DashboardEventBlock(event, currentTime)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    OverviewSectionHead(icon = Icons.Default.TaskAlt, title = "近期紧急未完成任务")
                }

                if (state.pendingTodos.isEmpty()) {
                    item {
                        EmptyStateCard("太棒了！所有任务均已完成。")
                    }
                } else {
                    items(state.pendingTodos.take(5)) { todo ->
                        DashboardTodoRow(todo)
                    }
                }
                
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun OverviewSectionHead(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun EmptyStateCard(message: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
            Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun DashboardEventBlock(event: TimelineEvent, currentTime: LocalTime) {
    val isPast = event.endTime.isBefore(currentTime)
    val isCurrent = event.startTime.isBefore(currentTime) && event.endTime.isAfter(currentTime)

    val bgColor = when (event.type) {
        com.smartscheduler.app.domain.model.EventType.FIXED -> FixedEventColor
        com.smartscheduler.app.domain.model.EventType.SCHEDULED -> ScheduledEventColor
    }

    val alpha = if (isPast) 0.3f else if (isCurrent) 0.3f else 0.15f
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor.copy(alpha = alpha))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(36.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (isPast) bgColor.copy(alpha=0.5f) else bgColor)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (isPast) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                ),
                color = if (isPast) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${event.startTime.format(timeFormatter)} - ${event.endTime.format(timeFormatter)}",
                style = MaterialTheme.typography.bodySmall,
                color = if (isPast) MaterialTheme.colorScheme.onSurfaceVariant else bgColor.copy(alpha = 0.8f)
            )
        }
        if (isCurrent) {
            Badge(containerColor = MaterialTheme.colorScheme.error) { Text("IN PROGRESS", color = Color.White) }
        }
    }
}

@Composable
private fun DashboardTodoRow(todo: Todo) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(10.dp).clip(CircleShape)
                    .background(if (todo.deadlineDate.isBefore(LocalDate.now())) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Deadline: ${todo.deadlineDate.format(DateTimeFormatter.ofPattern("MM-dd"))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (todo.estimatedMinutes > 0) {
                Text(
                    text = "${todo.estimatedMinutes}m",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
