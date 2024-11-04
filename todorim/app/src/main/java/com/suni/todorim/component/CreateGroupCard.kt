package com.suni.todorim.component

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * 그룹 생성 카드
 * @modifier: Modifier
 * @onClick: () -> Unit : 클릭 이벤트
 */
@Composable
fun CreateGroupCard(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(Color.White),
        onClick = onClick
    ) {
        Text(text = "페이지 생성하기!!!")
    }
}