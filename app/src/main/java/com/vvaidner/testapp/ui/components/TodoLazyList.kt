package com.vvaidner.testapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vvaidner.domain.model.Todo

@Composable
fun TodoLazyList(
    todoList: List<Todo>,
    modifier: Modifier = Modifier,
    onChecked: (message: String) -> Unit,
    state: LazyListState = rememberLazyListState(),
    verticalArrangement: Arrangement.Vertical = TodoLazyListDefaults.VerticalArrangement,
) {
    LazyColumn(
        modifier = modifier
            .padding(top = 60.dp)
            .semantics {
                testTag = TodoLazyListDefaults.TodoLazyListTestTag
            },
        state = state,
        verticalArrangement = verticalArrangement,
    ) {
        items(count = todoList.size) { index ->
            val todo = todoList[index]
            TodoItem(
                title = todo.title,
                checked = todo.completed,
                onCheckedChange = {
                    onChecked(todo.title)
                }
            )
        }
    }
}


object TodoLazyListDefaults {
    const val TodoLazyListTestTag = "todo_lazy_list"
    const val AppendProgressTestTag = "append_progress"
    const val ErrorTestTag = "error_tag"

    private val ItemsSpace: Dp = 16.dp
    val VerticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(space = ItemsSpace)
}
