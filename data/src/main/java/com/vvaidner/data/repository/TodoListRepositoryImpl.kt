package com.vvaidner.data.repository

import com.vvaidner.data.remote.TodosApi
import com.vvaidner.data.remote.schema.TodoScheme
import com.vvaidner.data.remote.schema.toDomain
import com.vvaidner.domain.model.Todo
import com.vvaidner.domain.repository.TodoListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class TodoListRepositoryImpl(
    private val api: TodosApi,
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
) : TodoListRepository {


    override fun getTodoList(): Flow<List<Todo>> {
        val flow = flow {
            val value = api.getTodos()

            emit(value = value)
        }
        return flow.map(List<TodoScheme>::toDomain).flowOn(context = coroutineContext)
    }
}