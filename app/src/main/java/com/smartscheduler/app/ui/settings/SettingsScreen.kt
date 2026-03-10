package com.smartscheduler.app.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()
    var baseUrl by remember(settings) { mutableStateOf(settings.baseUrl) }
    var apiKey by remember(settings) { mutableStateOf(settings.apiKey) }
    var modelName by remember(settings) { mutableStateOf(settings.modelName) }
    var customPrompt by remember(settings) { mutableStateOf(settings.customPrompt) }
    var showKey by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }
    var modelDropdownExpanded by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Predefined model options grouped by provider
    val modelOptions = listOf(
        "deepseek-chat" to "DeepSeek Chat",
        "deepseek-reasoner" to "DeepSeek Reasoner (R1)",
        "gpt-4o" to "GPT-4o",
        "gpt-4o-mini" to "GPT-4o Mini",
        "gpt-3.5-turbo" to "GPT-3.5 Turbo",
        "claude-3-5-sonnet-20241022" to "Claude 3.5 Sonnet",
        "qwen-turbo" to "通义千问 Turbo",
        "qwen-plus" to "通义千问 Plus",
        "glm-4" to "智谱 GLM-4",
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "设置",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            // API Configuration section
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row {
                        Icon(Icons.Default.Api, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "LLM API 配置",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    OutlinedTextField(
                        value = baseUrl,
                        onValueChange = { baseUrl = it },
                        label = { Text("服务地址 (Base URL)") },
                        placeholder = { Text("https://api.deepseek.com") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) }
                    )

                    OutlinedTextField(
                        value = apiKey,
                        onValueChange = { apiKey = it },
                        label = { Text("API 密钥") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (showKey) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Default.Key, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { showKey = !showKey }) {
                                Icon(
                                    if (showKey) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "切换可见性"
                                )
                            }
                        }
                    )

                    // Model dropdown
                    ExposedDropdownMenuBox(
                        expanded = modelDropdownExpanded,
                        onExpandedChange = { modelDropdownExpanded = !modelDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = modelName,
                            onValueChange = { modelName = it },
                            label = { Text("模型名称") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            leadingIcon = { Icon(Icons.Default.SmartToy, contentDescription = null) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelDropdownExpanded) },
                            supportingText = { Text("可从下拉列表选择，也可手动输入") }
                        )
                        ExposedDropdownMenu(
                            expanded = modelDropdownExpanded,
                            onDismissRequest = { modelDropdownExpanded = false }
                        ) {
                            modelOptions.forEach { (id, label) ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(label, style = MaterialTheme.typography.bodyMedium)
                                            Text(id, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    },
                                    onClick = {
                                        modelName = id
                                        modelDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Custom Prompt section
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row {
                        Icon(Icons.Default.EditNote, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "自定义 AI 提示词",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        "添加个人偏好来引导 AI 调度。例如：工作时间段、任务优先规则、休息偏好等。",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = customPrompt,
                        onValueChange = { customPrompt = it },
                        label = { Text("自定义指令") },
                        placeholder = { Text("例如：我喜欢晚上学习，上午安排较难的任务...") },
                        minLines = 4,
                        maxLines = 8,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Save button
            Button(
                onClick = {
                    viewModel.saveSettings(
                        settings.copy(
                            baseUrl = baseUrl,
                            apiKey = apiKey,
                            modelName = modelName,
                            customPrompt = customPrompt
                        )
                    )
                    scope.launch {
                        snackbarHostState.showSnackbar("设置已保存")
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("保存设置", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
            }

            // Danger zone
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "危险操作",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { showClearDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("清除所有数据")
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("确认清除所有数据？") },
            text = { Text("这将删除所有导入的日程、待办事项和 AI 生成的排程。此操作不可撤销。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllData()
                        showClearDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("所有数据已清除")
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("全部删除")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("取消") }
            }
        )
    }
}
