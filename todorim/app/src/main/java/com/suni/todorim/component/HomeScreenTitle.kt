package com.suni.todorim.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.suni.domain.getStrHomeDate

/**
 * 타이틀
 * @param modifier
 * @param settingOnClickEvent 설정 아이콘 클릭
 */
@Composable
fun HomeScreenTitle(
    modifier: Modifier,
    settingOnClickEvent: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = getStrHomeDate(),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        IconButton(onClick = settingOnClickEvent) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "setting",
                tint = Color.White,
            )
        }
    }
}