package com.suni.todo.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.suni.navigator.KEY_GROUP_COLOR_INDEX
import com.suni.navigator.KEY_GROUP_ID
import com.suni.navigator.KEY_TODO_FLAG
import com.suni.navigator.KEY_TODO_ID
import com.suni.navigator.KEY_TODO_MAX_ID
import com.suni.navigator.TodoScreenFlag
import com.suni.todo.ui.create.CreateTodoScreen
import com.suni.todo.ui.create.CreateTodoScreenViewModel
import com.suni.todo.ui.modify.ModifyTodoScreen
import com.suni.todo.ui.modify.ModifyTodoScreenViewModel
import com.suni.ui.theme.SuniTodorimTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * [ComponentActivity] 할일 화면 (상세/생성)
 * 24.09.10 Create class - Q
 */
@AndroidEntryPoint
class TodoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val todoScreenFlag =
            intent.getStringExtra(KEY_TODO_FLAG) ?: TodoScreenFlag.CREATE.name
        val groupId =
            intent.getIntExtra(KEY_GROUP_ID, 0)
        val todoId =
            intent.getIntExtra(KEY_TODO_ID, 0)
        val todoMaxId =
            intent.getIntExtra(KEY_TODO_MAX_ID, 0)
        val groupColorIndex =
            intent.getIntExtra(KEY_GROUP_COLOR_INDEX, 0)

        setContent {
            SuniTodorimTheme {
                when (todoScreenFlag) {
                    TodoScreenFlag.CREATE.name -> {
                        // 할 일 생성
                        val viewModel = hiltViewModel<CreateTodoScreenViewModel>()
                        CreateTodoScreen(
                            viewModel = viewModel,
                            groupId = groupId,
                            groupColorIndex = groupColorIndex,
                            todoMaxId = todoMaxId,
                        ) {
                            setResult(RESULT_OK)
                            finish()
                        }
                    }

                    TodoScreenFlag.MODIFY.name -> {
                        // 할 일 수정
                        val viewModel = hiltViewModel<ModifyTodoScreenViewModel>()
                        ModifyTodoScreen(
                            viewModel = viewModel,
                            todoId = todoId,
                            groupColorIndex = groupColorIndex,
                        ) {
                            setResult(RESULT_OK)
                            finish()
                        }
                    }

                    else -> {}
                }

            }
        }
    }
}