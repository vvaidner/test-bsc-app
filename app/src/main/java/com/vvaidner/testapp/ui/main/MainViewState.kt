package com.vvaidner.testapp.ui.main

import com.vvaidner.domain.model.Todo

data class MainViewState(
    val todoList: List<Todo> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null,
)