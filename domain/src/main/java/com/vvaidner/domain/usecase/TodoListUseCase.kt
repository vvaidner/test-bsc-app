package com.vvaidner.domain.usecase

import com.vvaidner.domain.core.UseCase
import com.vvaidner.domain.model.Todo
import com.vvaidner.domain.repository.TodoListRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class TodoListUseCase(
    coroutineContext: CoroutineContext,
    private val repository: TodoListRepository,
) : UseCase<TodoListUseCase.Param, List<Todo>>(coroutineContext = coroutineContext) {

    object Param {
        //nothing
    }

    override fun execute(param: Param): Flow<List<Todo>> {
        return repository.getTodoList()
    }
}