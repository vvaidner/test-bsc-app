package com.vvaidner.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vvaidner.domain.repository.TodoListRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.vvaidner.domain.Result
import com.vvaidner.domain.data.TestTodoList
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class TodoListUseCaseTest {

    private companion object {
        val TestParam = TodoListUseCase.Param
    }

    private val repository = mockk<TodoListRepository>()

    private val useCase = TodoListUseCase(
        coroutineContext = Dispatchers.IO,
        repository = repository,
    )

    @Test
    fun `Result flow emits Loading`() = runTest {
        every { repository.getTodoList() } returns flowOf(TestTodoList)

        useCase.resultFlow(param = TestParam).test {
            val actual = awaitItem()

            skipItems(count = 1)
            awaitComplete()

            Truth.assertThat(actual is Result.Loading).isTrue()
        }
    }

    @Test
    fun `Result flow emits Success with exchange rates`() = runTest {
        every { repository.getTodoList() } returns flowOf(TestTodoList)

        useCase.resultFlow(param = TestParam).test {
            skipItems(count = 1)

            val expected = Result.Success(value = TestTodoList)
            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `Result flow emits Error`() = runTest {
        every { repository.getTodoList() } returns flow { throw NullPointerException() }

        useCase.resultFlow(param = TestParam).test {
            skipItems(count = 1)

            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual is Result.Error).isTrue()
        }
    }
}