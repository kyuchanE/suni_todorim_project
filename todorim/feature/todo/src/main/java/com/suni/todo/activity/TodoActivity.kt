package com.suni.todo.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.suni.navigator.KEY_GROUP_ID
import com.suni.navigator.KEY_TODO_MAX_ID
import com.suni.todo.ui.create.CreateTodoScreen
import com.suni.todo.ui.create.CreateTodoScreenViewModel
import com.suni.ui.theme.SuniTodorimTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * [ComponentActivity] 할일 화면 (상세/생성)
 * 24.09.10 Create class - Q
 */
@AndroidEntryPoint
class TodoActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val groupId =
            intent.getIntExtra(KEY_GROUP_ID, 0)
        val todoMaxId =
            intent.getIntExtra(KEY_TODO_MAX_ID, 0)

        setContent {
            SuniTodorimTheme {
                val viewModel = hiltViewModel<CreateTodoScreenViewModel>()
                CreateTodoScreen(
                    viewModel = viewModel,
                    groupId = groupId,
                    todoMaxId = todoMaxId,
                ) { isNeedRefresh ->
                    if (isNeedRefresh)
                        setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }
}