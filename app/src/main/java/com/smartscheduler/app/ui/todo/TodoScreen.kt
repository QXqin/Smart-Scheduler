package com.smartscheduler.app.ui.todo

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartscheduler.app.domain.model.Todo
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TodoScreen(
    viewModel: TodoViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加待办")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header
            Text(
                text = "待办事项",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

            if (state.errorMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("发生错误", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onErrorContainer)
                        Text(state.errorMessage ?: "", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("关闭")
                        }
                    }
                }
            }

            if (state.todos.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.CheckCircleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "暂无待办事项",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "点击 + 添加新任务",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.todos, key = { it.id }) { todo ->
                        TodoItem(
                            todo = todo,
                            onToggle = { viewModel.toggleComplete(it) },
                            onEdit = { viewModel.startEdit(it) },
                            onDelete = { viewModel.deleteTodo(it) }
                        )
                    }
                }
            }
        }
    }

    // Add/Edit dialog
    if (state.showAddDialog) {
        TodoEditDialog(
            editing = state.editingTodo,
            onDismiss = { viewModel.hideDialog() },
            onSave = { title, desc, deadline, minutes, isDaily ->
                viewModel.saveTodo(title, desc, deadline, minutes, isDaily)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodoItem(
    todo: Todo,
    onToggle: (Todo) -> Unit,
    onEdit: (Todo) -> Unit,
    onDelete: (Todo) -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
    val isOverdue = !todo.isCompleted && todo.deadlineDate.isBefore(LocalDate.now())
    val cardColor by animateColorAsState(
        if (todo.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        else MaterialTheme.colorScheme.surfaceVariant,
        label = "cardColor"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        onClick = { onEdit(todo) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { onToggle(todo) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null
                        ),
                        color = if (todo.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (todo.isDaily) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            Icons.Default.Repeat,
                            contentDescription = "Daily Repeat",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Deadline
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = if (isOverdue) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = todo.deadlineDate.format(dateFormatter),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isOverdue) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    // Duration
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Timer,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val dur = if (todo.estimatedMinutes > 0) {
                            val h = todo.estimatedMinutes / 60
                            val m = todo.estimatedMinutes % 60
                            if (h > 0 && m > 0) "${h}h ${m}m"
                            else if (h > 0) "${h}h" else "${m}m"
                        } else {
                            "AI估算" // "AI will estimate"
                        }
                        Text(
                            text = dur,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            IconButton(onClick = { onDelete(todo) }) {
                Icon(
                    Icons.Default.DeleteOutline,
                    contentDescription = "删除",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodoEditDialog(
    editing: Todo?,
    onDismiss: () -> Unit,
    onSave: (title: String, description: String, deadline: LocalDate, estimatedMinutes: Int, isDaily: Boolean) -> Unit
) {
    var title by remember { mutableStateOf(editing?.title ?: "") }
    var description by remember { mutableStateOf(editing?.description ?: "") }
    var deadlineText by remember {
        mutableStateOf(editing?.deadlineDate?.toString() ?: LocalDate.now().plusDays(7).toString())
    }
    
    // Convert editing minutes to string like "1h30m" or leave blank if 0
    var durationText by remember {
        val mins = editing?.estimatedMinutes ?: 0
        val text = if (mins > 0) {
            val h = mins / 60
            val m = mins % 60
            if (h > 0 && m > 0) "${h}h${m}m" else if (h > 0) "${h}h" else "${m}m"
        } else ""
        mutableStateOf(text)
    }
    
    var isDaily by remember { mutableStateOf(editing?.isDaily ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (editing != null) "编辑任务" else "新建任务") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("标题") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("描述（可选）") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = deadlineText,
                    onValueChange = { deadlineText = it },
                    label = { Text("截止日期 (YYYY-MM-DD)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = durationText,
                    onValueChange = { durationText = it },
                    label = { Text("预计耗时 (选填)") },
                    placeholder = { Text("例如 1h 30m，留空则由 AI 估算") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("每日重复: ", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                    Switch(checked = isDaily, onCheckedChange = { isDaily = it })
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val deadline = try { LocalDate.parse(deadlineText) } catch (e: Exception) { LocalDate.now().plusDays(7) }
                    
                    // Parse duration text nicely
                    var totalMin = 0
                    val lower = durationText.lowercase().trim()
                    if (lower.isNotBlank()) {
                        val hMatch = Regex("(\\d+)\\s*h").find(lower)
                        val mMatch = Regex("(\\d+)\\s*m").find(lower)
                        val h = hMatch?.groupValues?.get(1)?.toIntOrNull() ?: 0
                        val m = mMatch?.groupValues?.get(1)?.toIntOrNull() ?: 0
                        
                        // Fallback if just a number was typed (assume minutes)
                        if (h == 0 && m == 0) {
                            totalMin = lower.toIntOrNull() ?: 0
                        } else {
                            totalMin = h * 60 + m
                        }
                    }

                    if (title.isNotBlank()) {
                        // Allow totalMin to be 0 (AI estimates)
                        onSave(title, description, deadline, totalMin, isDaily)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )
}
