package com.vvaidner.testapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvaidner.testapp.core.ViewState
import com.vvaidner.testapp.core.WhileUiSubscribed
import com.vvaidner.domain.Result
import com.vvaidner.domain.asResult
import com.vvaidner.domain.model.Todo
import com.vvaidner.domain.usecase.TodoListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MainViewModel(
    private val todoListUseCase: TodoListUseCase
) : ViewModel() {

    private val error = MutableStateFlow<Throwable?>(value = null)
    private val isLoading = MutableStateFlow(value = false)
    private val todoList = MutableStateFlow<List<Todo>>(value = emptyList())

    private val todoListResult: Flow<Result<List<Todo>>> =
        todoListUseCase.resultFlow(param = TodoListUseCase.Param)

    val uiState: StateFlow<MainViewState> = combine(
        todoList,
        isLoading,
        error,
    ) { todoList, isLoading, error ->
        MainViewState(
            todoList = todoList,
            isLoading = isLoading,
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = MainViewState(),
    )

    fun loadTodoList() {
        viewModelScope.launch {
            todoListResult.collectLatest { todoListResult ->
                when (todoListResult) {
                    Result.Loading -> {
                        isLoading.value = true
                    }

                    is Result.Error -> {
                        isLoading.value = false
                        error.value = todoListResult.exception
                    }

                    is Result.Success -> {
                        isLoading.value = false
                        todoList.value = todoListResult.value
                    }
                }
            }
        }
    }

    fun addNewTodoItem(title: String) {
        val newTodoList = mutableListOf<Todo>().apply {
            add(
                Todo(
                    userId = 0,
                    id = 0,
                    title = title,
                    completed = false
                )
            )
            addAll(todoList.value)
        }
        todoList.value = newTodoList
    }

    fun onTodoItemCompleted(title: String) {
        val newTodoList = mutableListOf<Todo>()
        todoList.value.forEach { todo ->
            if (todo.title == title) {
                newTodoList.add(
                    todo.copy(
                        completed = !todo.completed
                    )
                )
            } else {
                newTodoList.add(todo)
            }
        }
        todoList.value = newTodoList
    }

    fun onError(error: Throwable) {
        viewModelScope.launch { this@MainViewModel.error.emit(value = error) }
    }

    fun onErrorConsumed() {
        viewModelScope.launch { error.emit(value = null) }
    }

    private companion object {
        const val PARAM_CHANGE_DEBOUNCE = 400L
    }
}