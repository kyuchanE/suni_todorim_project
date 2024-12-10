package com.suni.todogroup.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suni.todogroup.R

/**
 * 최상단 타이틀 - 할 일 추가/수정
 * @param modifier Modifier
 * @param isCreateMode Boolean - 할 일 추가 모드 인지 여부
 * @param onCloseBtnClick () -> Unit - 닫기 버튼 클릭 이벤트
 */
@Composable
fun TodoTitle(
    modifier: Modifier,
    isCreateMode: Boolean = true,
    onCloseBtnClick: () -> Unit = {},
) {
    val strTitle =
        if (isCreateMode) stringResource(id = R.string.create_todo_title)
        else stringResource(id = R.string.modify_todo_title)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = strTitle,
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(com.suni.ui.R.color.tdr_default),
        )
        IconButton(onClick = onCloseBtnClick) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close Todo",
                tint = colorResource(com.suni.ui.R.color.tdr_default),
            )
        }
    }
}