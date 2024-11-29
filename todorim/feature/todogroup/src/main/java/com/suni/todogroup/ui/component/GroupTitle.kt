package com.suni.todogroup.ui.component

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
import com.suni.domain.findActivity
import com.suni.todogroup.R

/**
 * 그룹 최상단 타이틀
 * @param context
 * @param modifier
 */
@Composable
fun GroupTitle(
    context: Context,
    modifier: Modifier,
    isCreateMode: Boolean = true,
) {
    val strTitle =
        if (isCreateMode) stringResource(id = R.string.create_group_title)
        else stringResource(id = R.string.modify_group_title)

    Row(
        modifier = modifier.padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = strTitle,
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(com.suni.ui.R.color.tdr_default),
        )
        IconButton(onClick = { context.findActivity().finish() }) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close Group",
                tint = colorResource(com.suni.ui.R.color.tdr_default),
            )
        }
    }
}