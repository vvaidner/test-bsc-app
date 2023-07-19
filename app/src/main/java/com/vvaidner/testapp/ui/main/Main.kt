package com.vvaidner.testapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.vvaidner.testapp.R
import com.vvaidner.testapp.ui.components.TodoLazyList
import com.vvaidner.testapp.ui.theme.Purple40
import org.koin.androidx.compose.getViewModel

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun Main(
    viewModel: MainViewModel = getViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val unknownErrorText = stringResource(id = R.string.unknown_error_remote)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTodoList()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(R.string.todolist),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.inversePrimary)
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .semantics {
                        testTag = MainScreenDefaults.ContentErrorTestTag
                    }
                    .imePadding()
                    .navigationBarsPadding(),
                hostState = snackbarHostState,
            )
        },
    ) { paddingValues ->
        Content(
            modifier = Modifier.padding(paddingValues = paddingValues),
            viewState = uiState,
            onError = { error ->
                LaunchedEffect(snackbarHostState) {
                    viewModel.onErrorConsumed()
                    snackbarHostState.showSnackbar(
                        message = error.localizedMessage ?: unknownErrorText
                    )
                }
            },
            onAddNewTodoItem = { title ->
                viewModel.addNewTodoItem(title)
            },
            onTodoItemComplete = { title ->
                viewModel.onTodoItemCompleted(title)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    modifier: Modifier,
    viewState: MainViewState,
    onError: @Composable (error: Throwable) -> Unit,
    onAddNewTodoItem: (title: String) -> Unit,
    onTodoItemComplete: (title: String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .background(color = Color.White)
            .fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .align(Alignment.TopCenter)
        ) {
            TextField(
                value = text,
                onValueChange = {
                    text = it
                },
                label = { Text(text = stringResource(R.string.add_todo)) },
            )
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onAddNewTodoItem(text)
                        text = ""
                    }
                }
            ) {
                Text(text = stringResource(R.string.add))
            }
        }
        TodoLazyList(
            modifier = Modifier.fillMaxSize(),
            todoList = viewState.todoList,
            onChecked = { message: String ->
                onTodoItemComplete(message)
            }
        )

        if (viewState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    viewState.error?.run {
        onError(error = this)
    }
}


object MainScreenDefaults {
    const val ContentErrorTestTag = "content_error"
}