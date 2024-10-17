package com.suni.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 *  시간 선택
 */
@Composable
fun TdrTimePickerContainer(
    modifier: Modifier,
    onBottomButtonClickEvent: (selectedValue: String) -> Unit = {_ ->},
) {
    val typeValues = remember { mutableListOf("오전", "오후") }
    val typePickerState = rememberPickerState()
    val hourValues = remember { (1..12).map { it.toString() } }
    val hourPickerState = rememberPickerState()
    val minuteValues = remember { (0..59 step 5).map { it.toString() } }
    val minutePickerState = rememberPickerState()

    Column(
        modifier = modifier.height(155.dp),
    ) {
        // 시간 선택
        Row(
            modifier = modifier,
        ) {
            // AM/PM
            TdrTimePicker(
                modifier = Modifier.weight(2f),
                items = typeValues,
                state = typePickerState,
            )
            // 시간 선택
            TdrPicker(
                modifier = Modifier.weight(1f),
                items = hourValues,
                state = hourPickerState,
            )
            // 분 선택
            TdrPicker(
                modifier = Modifier.weight(1f),
                items = minuteValues,
                state = minutePickerState,
            )
        }
        Spacer(modifier = Modifier.height(55.dp))
        // 확인 버튼
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onBottomButtonClickEvent(
                    hourPickerState.selectedItem +
                            ":" +
                            minutePickerState.selectedItem
                )
            },
        ) {
            Text(text = "확인")
        }
    }


}

/**
 * 날짜 선택
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TdrDatePicker(
    modifier: Modifier,
    yearNow: Int,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(yearRange = IntRange(yearNow, yearNow + 1))

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
