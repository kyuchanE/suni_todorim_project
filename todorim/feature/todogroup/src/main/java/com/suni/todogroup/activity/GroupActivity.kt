package com.suni.todogroup.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.suni.navigator.KEY_GROUP_FLAG
import com.suni.navigator.GroupScreenFlag
import com.suni.navigator.TodoNavigator
import com.suni.todogroup.ui.create.CreateGroupScreen
import com.suni.todogroup.ui.detail.GroupDetailScreen
import com.suni.ui.theme.SuniTodorimTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * [ComponentActivity] 그룹 (생성/상세)
 * 24.09.11 Create class - Q
 */
@AndroidEntryPoint
class GroupActivity : ComponentActivity(){

    @Inject
    lateinit var todoNavigator: TodoNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val groupScreenFlag =
            intent.getStringExtra(KEY_GROUP_FLAG) ?: GroupScreenFlag.DETAIL.name

        setContent {
            SuniTodorimTheme {
                when(groupScreenFlag) {
                    GroupScreenFlag.CREATE.name -> {
                        CreateGroupScreen()
                    }
                    GroupScreenFlag.DETAIL.name -> {
                        GroupDetailScreen(
                            todoNavigatorAction = {
                                todoNavigator.navigateFrom(
                                    activity = this,
                                    withFinish = false,
                                )
                            }
                        )
                    }
                    else -> {
                        Spacer(modifier = Modifier
                            .height(20.dp)
                            .fillMaxHeight())
                    }
                }
            }
        }
    }
}