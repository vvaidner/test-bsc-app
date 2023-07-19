package com.vvaidner.testapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vvaidner.testapp.ui.theme.DoneColor
import com.vvaidner.testapp.ui.theme.TestAppTheme

@Composable
fun TodoItem(
    modifier: Modifier = Modifier,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(modifier = modifier.background(
        color = if(checked) DoneColor else Color.White
    )) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .align(Alignment.CenterVertically)
                .weight(1f),
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Checkbox(
            modifier = Modifier.padding(end = 16.dp),
            checked = checked, onCheckedChange = {
                onCheckedChange(it)
            })
    }
}

@Preview
@Composable
fun TodoItemPreview() {
    val checked = remember {
        mutableStateOf(false)
    }
    TestAppTheme {
        TodoItem(
            title = "quis ut nam facilis et officia qui quis ut nam facilis et officia qui",
            checked = checked.value,
            onCheckedChange = {
                checked.value = it
            }
        )
    }
}