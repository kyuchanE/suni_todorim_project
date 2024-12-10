package com.suni.todogroup.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suni.todogroup.R
import com.suni.todogroup.ui.create.CreateGroupScreenEvents
import com.suni.ui.component.GradientButton

/**
 * 그룹 생성/수정 하단 버튼
 * @param modifier
 * @param selectedColorIndex
 * @param isCreateMode      생성/수정
 * @param onClickDeleteEvent    그룹 삭제 - 수정일때만
 * @param onClickEvent      그룹 생성 및 수정
 */
@Composable
fun GroupBottomButton(
    modifier: Modifier,
    selectedColorIndex: Int,
    isCreateMode: Boolean = true,
    onClickDeleteEvent: () -> Unit = {},
    onClickEvent: () -> Unit = {}
) {
    Row(
        modifier = modifier,
    ) {
        if(!isCreateMode) {
            // 그룹 제거w
            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(65.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(15.dp),
                contentPadding = PaddingValues(),
                onClick = onClickDeleteEvent,
            ) {
                Box(
                    modifier = Modifier
                        .background(color = colorResource(id = com.suni.ui.R.color.tdr_red))
                        .then(modifier),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = stringResource(id = R.string.delete_group))
                }
            }
        }
        // 그룹 생성 / 수정
        GradientButton(
            text = stringResource(id = if(isCreateMode) R.string.str_add else R.string.str_modify),
            textColor = Color.White,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(65.dp)
                .weight(1f),
            selectedColorIndex = selectedColorIndex,
            onClick = onClickEvent,
        )
    }
}