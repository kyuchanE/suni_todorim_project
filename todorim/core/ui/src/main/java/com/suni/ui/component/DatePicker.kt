package com.suni.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/**
 *  날짜 선택 (년, 월, 일)
 */
@Composable
fun TdrDatePicker(
    modifier: Modifier,
    yearNow: Int,
) {
    val yearValues = remember { (yearNow..yearNow+1).map { it.toString() } }
    val yearPickerState = rememberPickerState()
    val monthValues = remember { (1..12).map { it.toString() } }
    val monthPickerState = rememberPickerState()
    val dayValues = remember { (1..31).map { it.toString() } }    // 월마다 최대 일수가 다름
//    var dayValues by remember { mutableStateOf("") }    // 월마다 최대 일수가 다름

    Row(
        modifier = modifier,
    ) {
        // 년도 선택
        TdrPicker(modifier = Modifier.weight(2f), items = yearValues)
        // 월 선택
        TdrPicker(modifier = Modifier.weight(1f), items = monthValues)
        // 일 선택
        TdrPicker(modifier = Modifier.weight(1f), items = dayValues)
    }

}
