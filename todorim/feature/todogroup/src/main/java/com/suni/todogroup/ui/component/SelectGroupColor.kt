package com.suni.todogroup.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.suni.ui.component.ColorPalette

/**
 * 그룹 색상 선택 및 타이틀
 * @param modifier
 */
@Composable
fun SelectGroupColor(
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