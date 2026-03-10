package com.smartscheduler.app.ui.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartscheduler.app.data.repository.TodoRepository
import com.smartscheduler.app.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class TodoUiState(
    val todos: List<Todo> = emptyList(),
    val showAddDialog: Boolean = false,
    val editingTodo: Todo? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _showDialog = MutableStateFlow(false)
    private val _editingTodo = MutableStateFlow<Todo?>(null)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<TodoUiState> = combine(
        todoRepository.getAllTodos().catch { e ->
            _errorMessage.value = "DB加载错: ${e.message}\n${e.stackTraceToString().take(200)}"
            emit(emptyList<Todo>())
        },
        _showDialog,
        _editingTodo,
        _errorMessage
    ) { todos, showDialog, editing, errorMsg ->
        TodoUiState(
            todos = todos,
            showAddDialog = showDialog,
            editingTodo = editing,
            errorMessage = errorMsg
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodoUiState())

    fun clearError() { _errorMessage.value = null }
    fun showAddDialog() { _showDialog.value = true }
    fun hideDialog() { _showDialog.value = false; _editingTodo.value = null }

    fun startEdit(todo: Todo) {
        _editingTodo.value = todo
        _showDialog.value = true
    }

    fun saveTodo(
        title: String,
        description: String,
        deadline: LocalDate,
        estimatedMinutes: Int,  // 0 = AI will estimate
        isDaily: Boolean
    ) {
        viewModelScope.launch {
            val editing = _editingTodo.value
            if (editing != null) {
                todoRepository.updateTodo(
                    editing.copy(
                        title = title,
                        description = description,
                        deadlineDate = deadline,
                        estimatedMinutes = estimatedMinutes,
                        isDaily = isDaily
                    )
                )
            } else {
                todoRepository.insertTodo(
                    Todo(
                        title = title,
                        description = description,
                        deadlineDate = deadline,
                        estimatedMinutes = estimatedMinutes,
                        isDaily = isDaily
                    )
                )
            }
            _showDialog.value = false
            _editingTodo.value = null
        }
    }

    fun toggleComplete(todo: Todo) {
        viewModelScope.launch {
            if (todo.isDaily) {
                // Daily task: advance deadline by 1 day instead of marking done
                todoRepository.advanceDailyTodo(todo)
            } else {
                todoRepository.updateTodo(todo.copy(isCompleted = !todo.isCompleted))
            }
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch { todoRepository.deleteTodo(todo) }
    }
}
