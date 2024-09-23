package com.suni.todogroup.ui.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.suni.domain.findActivity
import com.suni.navigator.TodoNavigator

@Composable
fun GroupDetailScreen(
    viewModel: GroupDetailScreenViewModel,
    todoNavigatorAction: () -> Unit = {},
    groupId: Int,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onEvent(GroupDetailScreenEvents.LoadGroupData(groupId))
    }

    Scaffold { pv ->
        Surface(
            modifier = Modifier.padding(pv),
            color = Color.Magenta,
        ) {
            val title = if(viewModel.state.groupData == null) {
                ""
            } else {
                viewModel.state.groupData!!.title
            }
            
            Row {
                Button(onClick = {
                    context.findActivity().finish()
                }) {
                    Text(text = title)
                }
                Button(onClick = {
                    todoNavigatorAction()
                }) {
                    Text(text = "Move TodoScreen")
                }
            }
        }

    }
}