package com.smartscheduler.app.ui.calendar

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartscheduler.app.ui.theme.FixedEventColor
import com.smartscheduler.app.ui.theme.ScheduledEventColor
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showImportDialog by remember { mutableStateOf(false) }
    var showUrlDialog by remember { mutableStateOf(false) }

    val icsPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { viewModel.importIcs(it) }
    }

    // Show messages as snackbar
    LaunchedEffect(state.message) {
        state.message?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Import FAB
                SmallFloatingActionButton(
                    onClick = { showImportDialog = true },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Icon(Icons.Default.FileOpen, contentDescription = "Import ICS")
                }
                // Generate Schedule FAB
                ExtendedFloatingActionButton(
                    onClick = { viewModel.generateSchedule() },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    icon = {
                        if (state.isGenerating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        }
                    },
                    text = {
                        Text(if (state.isGenerating) "Generating..." else "AI Schedule")
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Week header with day selector
            WeekHeader(
                selectedDate = state.selectedDate,
                onDateSelected = { viewModel.selectDate(it) }
            )

            // Legend
            EventLegend()

            // Day schedule with pager for swipe
            DayPager(
                selectedDate = state.selectedDate,
                state = state,
                onDateChanged = { viewModel.selectDate(it) }
            )
        }
    }

    // Import source dialog
    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = { Text("Import Calendar") },
            text = { Text("Import schedule events from a .ics file or a network URL") },
            confirmButton = {
                TextButton(onClick = {
                    showImportDialog = false
                    icsPickerLauncher.launch(arrayOf("text/calendar", "*/*"))
                }) {
                    Text("Local File")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImportDialog = false
                    showUrlDialog = true
                }) {
                    Text("Network URL")
                }
            }
        )
    }

    // URL input dialog
    if (showUrlDialog) {
        var url by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showUrlDialog = false },
            title = { Text("Import from URL") },
            text = {
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("Calendar URL") },
                    placeholder = { Text("https://example.com/calendar.ics") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (url.isNotBlank()) {
                        viewModel.importIcsFromUrl(url)
                        showUrlDialog = false
                    }
                }) {
                    Text("Import")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUrlDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Week offset correction dialog — shown after a successful parse
    if (state.showWeekOffsetDialog) {
        WeekOffsetDialog(
            eventCount = state.pendingImportCount,
            onConfirm = { week -> viewModel.confirmImportWithWeekOffset(week) },
            onDismiss = { viewModel.cancelImport() }
        )
    }
}

// ---------------------------------------------------------------------------
// Week Offset Dialog
// ---------------------------------------------------------------------------

@Composable
private fun WeekOffsetDialog(
    eventCount: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var currentWeek by remember { mutableIntStateOf(1) }
    val today = LocalDate.now()

    // Compute estimated semester start date live
    val semesterStart = remember(currentWeek) {
        val todayMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        todayMonday.minusWeeks((currentWeek - 1).toLong())
    }
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                "Set Current Semester Week",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "Parsed $eventCount course events. Select which semester week " +
                            "today (${today.format(dateFormatter)}) belongs to so the " +
                            "app can align the course schedule correctly.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Week stepper
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { if (currentWeek > 1) currentWeek-- },
                            enabled = currentWeek > 1
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease")
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Week $currentWeek",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "of semester",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = { if (currentWeek < 30) currentWeek++ },
                            enabled = currentWeek < 30
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }
                }

                // Estimated semester start preview
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Est. semester start: ${semesterStart.format(dateFormatter)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(currentWeek) },
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Confirm & Import")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.error)
            }
        }
    )
}

// ---------------------------------------------------------------------------
// Existing private composables (unchanged)
// ---------------------------------------------------------------------------

@Composable
private fun WeekHeader(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val monday = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
    val today = LocalDate.now()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Month title
        Text(
            text = selectedDate.format(monthFormatter),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Week day row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0..6) {
                val date = monday.plusDays(i.toLong())
                val isSelected = date == selectedDate
                val isToday = date == today
                val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onDateSelected(date) }
                        .then(
                            if (isSelected) Modifier.background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                RoundedCornerShape(12.dp)
                            ) else Modifier
                        )
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = dayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .then(
                                if (isToday) Modifier.background(
                                    MaterialTheme.colorScheme.primary,
                                    CircleShape
                                ) else Modifier
                            )
                    ) {
                        Text(
                            text = "${date.dayOfMonth}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = when {
                                isToday -> MaterialTheme.colorScheme.onPrimary
                                isSelected -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EventLegend() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LegendItem(color = FixedEventColor, label = "Fixed Course")
        LegendItem(color = ScheduledEventColor, label = "AI Schedule")
    }
}

@Composable
private fun LegendItem(color: androidx.compose.ui.graphics.Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DayPager(
    selectedDate: LocalDate,
    state: CalendarUiState,
    onDateChanged: (LocalDate) -> Unit
) {
    // Use a pager centered on today, allowing +/- 365 days
    val totalPages = 731 // 365 days back + today + 365 days forward
    val centerPage = 365
    val today = LocalDate.now()

    val pagerState = rememberPagerState(
        initialPage = centerPage + (selectedDate.toEpochDay() - today.toEpochDay()).toInt(),
        pageCount = { totalPages }
    )

    val scope = rememberCoroutineScope()

    // Sync pager with selected date
    LaunchedEffect(selectedDate) {
        val targetPage = centerPage + (selectedDate.toEpochDay() - today.toEpochDay()).toInt()
        if (pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    // Update selected date when page changes
    LaunchedEffect(pagerState.currentPage) {
        val pageDate = today.plusDays((pagerState.currentPage - centerPage).toLong())
        if (pageDate != selectedDate) {
            onDateChanged(pageDate)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val pageDate = today.plusDays((page - centerPage).toLong())
        // Only render the currently visible page with real data, others show date header
        if (pageDate == state.selectedDate) {
            DayScheduleView(
                events = state.events,
                currentTime = if (pageDate == today) state.currentTime else null,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            DayScheduleView(
                events = emptyList(),
                currentTime = if (pageDate == today) LocalTime.now() else null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
