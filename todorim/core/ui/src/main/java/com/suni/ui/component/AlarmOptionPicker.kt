package com.suni.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suni.ui.R
import java.text.SimpleDateFormat
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

/**
 *  시간 선택
 *  @param onBottomButtonClickEvent 선택 완료 후 이벤트
 */
@Composable
fun TdrTimePickerContainer(
    modifier: Modifier,
    onBottomButtonClickEvent: (selectedValue: String) -> Unit = { _ -> },
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
            modifier = Modifier.fillMaxWidth(),
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
                    timePickerValue(
                        typePickerState.selectedItem,
                        hourPickerState.selectedItem,
                        minutePickerState.selectedItem,
                    )
                )
            },
        ) {
            Text(text = "확인")
        }
    }
}

enum class SelectOnePickerType(val valueList: List<String>) {
    DAY_OF_WEEK(
        listOf("일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일")
    ),
    DAY(
        (1..31).map { it.toString() + "일" }
    )
}

/**
 *  요일/일 선택
 *  @param type SelectOnePickerType - 요일 또는 일
 */
@Composable
fun TdrSelectOnePickerContainer(
    modifier: Modifier,
    type: SelectOnePickerType,
    onBottomButtonClickEvent: (selectedValue: Int) -> Unit = { _ -> },
) {
    val pickerState = rememberPickerState()

    Column(
        modifier = modifier.height(155.dp),
    ) {
        // 단일 값 선택
        TdrPicker(
            modifier = Modifier.fillMaxWidth(),
            items = type.valueList,
            state = pickerState,
        )
        Spacer(modifier = Modifier.height(55.dp))
        // 확인 버튼
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                try {
                    onBottomButtonClickEvent(
                        type.valueList.indexOf(pickerState.selectedItem) + 1
                    )
                } catch (e: Exception) {

                }
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
    onDateSelected: (date: Date) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(yearRange = IntRange(yearNow, yearNow + 1))

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    onDateSelected(convertMillisToDate(it))
                }
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
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = stringResource(R.string.title_date_picker),
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(R.color.tdr_default)
                )
            },
            headline = {
                datePickerState.selectedDateMillis?.let {
                    val date = convertMillisToDate(it)
                    val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = formatter.format(date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.tdr_default),
                    )
                } ?: Text(
                    modifier = Modifier.padding(20.dp),
                    text = stringResource(R.string.head_line_date_picker),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.tdr_default),
                )
            },
        )

    }
}

/**
 * 시간 피커 선택 값
 */
private fun timePickerValue(
    type: String,
    hour: String,
    minute: String,
): String {
    // TODO chan 00시와 12시 구분 필요
    return if (type == "오후") {
        "${hour.toInt() + 12}:$minute"
    } else {
        "$hour:$minute"
    }
}


fun convertMillisToDate(millis: Long): Date = Date(millis)
