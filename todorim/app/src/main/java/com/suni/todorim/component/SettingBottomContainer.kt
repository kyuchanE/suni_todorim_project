package com.suni.todorim.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suni.todorim.R

/**
 * 설정 모달 뷰
 */
@Composable
fun SettingBottomContainer(
    modifier: Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
    }
}