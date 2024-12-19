package com.suni.todogroup.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.suni.todogroup.R
import com.suni.ui.component.GradientButton

/**
 * 그룹 생성/수정 하단 버튼
 * @param modifier
 * @param selectedColorIndex
 * @param isCreateMode      생성/수정
 * @param onClickEvent      그룹 생성 및 수정
 */
@Composable
fun GroupBottomButton(
    modifier: Modifier,
    selectedColorIndex: Int,
    isCreateMode: Boolean = true,
    onClickEvent: () -> Unit = {}
) {
    // 그룹 생성 / 수정
    GradientButton(
        text = stringResource(id = if(isCreateMode) R.string.str_add else R.string.str_modify),
        textColor = Color.White,
        modifier = modifier,
        selectedColorIndex = selectedColorIndex,
        onClick = onClickEvent,
    )
}