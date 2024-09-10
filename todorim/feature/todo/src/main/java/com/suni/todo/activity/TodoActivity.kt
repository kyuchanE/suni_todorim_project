package com.suni.todo.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.suni.todo.ui.detail.TodoDetailScreen
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

        setContent {
            SuniTodorimTheme {
                TodoDetailScreen()
            }
        }
    }
}