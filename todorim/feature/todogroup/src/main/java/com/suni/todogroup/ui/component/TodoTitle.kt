package com.suni.todogroup.ui.component

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.suni.domain.findActivity
import com.suni.todogroup.R

/**
 * 최상단 타이틀 - 할 일 추가/수정
 *
 */
@Composable
fun TodoTitle(
    context: Context,
    modifier: Modifier,
    isCreateMode: Boolean = true,
) {
    val strTitle =
        if (isCreateMode) stringResource(id = R.string.create_todo_title)
        else stringResource(id = R.string.modify_todo_title)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = strTitle)

        IconButton(onClick = {
            context.findActivity().finish()
        }) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
            )
        }
    }
}