package com.suni.todorim

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.suni.todorim.ui.HomeScreen
import com.suni.todorim.ui.HomeScreenViewModel
import com.suni.ui.theme.SuniTodorimTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * [ComponentActivity] 메인 화면
 * 24.09.02 Create class - Q
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SuniTodorimTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = hiltViewModel<HomeScreenViewModel>()
                    HomeScreen(viewModel = viewModel)
                }
            }
        }
    }
}