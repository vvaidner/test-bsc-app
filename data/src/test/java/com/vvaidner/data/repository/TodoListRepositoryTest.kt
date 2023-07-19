package com.vvaidner.data.repository

import com.google.common.truth.Truth
import com.vvaidner.data.data.TodoTestSchema
import com.vvaidner.data.remote.TodosApi
import com.vvaidner.data.remote.schema.toDomain
import com.vvaidner.domain.repository.TodoListRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoListRepositoryTest {

    private val api = mockk<TodosApi>()

    private val repository: TodoListRepository = TodoListRepositoryImpl(
        api = api,
    )

    @Test
    fun `Get todo list success`() = runTest {
        val expected = TodoTestSchema.toDomain()

        coEvery { api.getTodos() } returns listOf(TodoTestSchema)

        val actual = repository.getTodoList().first()

        Truth.assertThat(actual).isNotEmpty()
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `Get todo list failure`() = runTest {
        coEvery { api.getTodos() } throws Exception()

        repository.getTodoList().first()
    }
}