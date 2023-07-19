package com.vvaidner.domain.repository

import com.vvaidner.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoListRepository {
    fun getTodoList(): Flow<List<Todo>>
}