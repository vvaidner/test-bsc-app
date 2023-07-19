package com.vvaidner.domain.data

import com.vvaidner.domain.model.Todo

val TestTodo = Todo(
    userId = 0, id = 0, title = "title", completed = false

)

val TestTodoList = listOf(
    TestTodo,
    TestTodo,
    TestTodo,
)