package com.suni.todogroup.ui.create

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suni.domain.findActivity
import com.suni.todogroup.R
import com.suni.ui.component.ColorPalette
import com.suni.ui.component.GradientButton

/**
 * 그룹 생성 화면
 * @param viewModel
 * @param finishedCreateAction
 */
@Composable
fun CreateGroupScreen(
    viewModel: CreateGroupScreenViewModel,
    maxGroupId: Int = 0,
    maxOrderId: Int = 0,
    finishedCreateAction: () -> Unit = { },
) {
    val context = LocalContext.current

    val strGroupTitle = remember { mutableStateOf("") }
    val currentFocus = LocalFocusManager.current

    Scaffold { pv ->
        Surface(
            modifier = Modifier
                .padding(pv)
                .fillMaxWidth()
                .fillMaxHeight(),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember{ MutableInteractionSource() },
                        indication = null
                    ) {
                        currentFocus.clearFocus()
                    }
            ) {
                // 최상단 타이틀
                CreateGroupTitle(
                    context = context,
                    modifier = Modifier
                        .fillMaxWidth()

                )
                // 그룹 색상 선택
                SelectGroupColor(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    strTitle = strGroupTitle,
                    titleChangeEvent = { str ->
                        strGroupTitle.value = str
                    },
                    selectedColorIndex = viewModel.state.colorIndex,
                    selectedColorEvent = { colorIndex: Int ->
                        viewModel.onEvent(
                            CreateGroupScreenEvents.SelectGroupColor(colorIndex)
                        )
                    }
                )
                // 그룹 생성 버튼
                GradientButton(
                    text = stringResource(id = R.string.str_add),
                    textColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(65.dp),
                    selectedColorIndex = viewModel.state.colorIndex
                ) {
                    viewModel.onEvent(
                        CreateGroupScreenEvents.CreateGroup(
                            groupId = maxGroupId,
                            order = maxOrderId,
                            title = strGroupTitle.value,
                            appColorIndex = viewModel.state.colorIndex,
                        )
                    )
                    finishedCreateAction()
                }
            }
        }

    }
}

/**
 * 최상단 타이틀
 * @param context
 * @param modifier
 */
@Composable
private fun CreateGroupTitle(
    context: Context,
    modifier: Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(24.dp))
        Text(text = stringResource(id = R.string.create_group_title))
        IconButton(onClick = { context.findActivity().finish() }) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
            )
        }
    }
}

/**
 * 그룹 색상 선택 및 타이틀
 * @param modifier
 */
@Composable
private fun SelectGroupColor(
    modifier: Modifier,
    strTitle: MutableState<String>,
    titleChangeEvent: (title:String) -> Unit = { _ -> },
    selectedColorIndex: Int = 0,
    selectedColorEvent: (colorIndex:Int) -> Unit = { _ -> },
) {
    Column(
        modifier = modifier,
    ) {
        TextField(
            value = strTitle.value,
            onValueChange = { titleChangeEvent(it) }
        )
        Spacer(modifier = Modifier.height(25.dp))
        ColorPalette(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            selectedIndex = selectedColorIndex,
            selectColorEvent = selectedColorEvent
        )
    }
}
