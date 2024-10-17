package com.suni.todo.ui.component

import com.suni.ui.component.endColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suni.domain.getTimeNow
import com.suni.domain.toDate
import com.suni.domain.toFullString
import com.suni.ui.R
import com.suni.ui.component.BottomArrowSelectBox
import com.suni.ui.component.TdrTimePickerContainer
import com.suni.ui.component.TdrDatePicker
import kotlinx.coroutines.launch

enum class TypeTimeRepeating(val titleStrId: Int) {
    NONE(R.string.time_repeating_none),
    DAY(R.string.time_repeating_every_day),
    WEEK(R.string.time_repeating_every_week),
    MONTH(R.string.time_repeating_every_month),
}

/**
 * 특정 시간 알림 설정
 * @param onCheckedChangedEvent 시간 알림 설정 후
 * @param onTypeClickEvent 반복 알림 설정 후
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeAlarmContainer(
    modifier: Modifier,
    isChecked: Boolean = false,
    type: TypeTimeRepeating = TypeTimeRepeating.NONE,
    selectedTypeOption: String = "",
    selectedTimeOption: String = "",
    colorIndex: Int,
    onCheckedChangedEvent: (checked: Boolean) -> Unit = {_ ->},
    onTypeClickEvent: (type: TypeTimeRepeating) -> Unit = {_ ->}
) {

    var repeatingTypeNoneOption by remember { mutableStateOf("") }
    var repeatingTypeWeekOption by remember { mutableStateOf("") }
    var repeatingTypeMonthOption by remember { mutableStateOf("") }
    var repeatingTimeOption by remember { mutableStateOf("") }

    var showTypeOptionBottomSheet by remember { mutableStateOf(false) }
    var showTimeBottomSheet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(selectedTimeOption.isNotEmpty()
            || selectedTypeOption.isNotEmpty()) {
        if (selectedTimeOption.isNotEmpty())
            repeatingTimeOption = selectedTimeOption

        if (selectedTypeOption.isNotEmpty()) {
            when(type) {
                TypeTimeRepeating.NONE -> repeatingTypeNoneOption = selectedTypeOption
                TypeTimeRepeating.WEEK -> repeatingTypeWeekOption = selectedTypeOption
                TypeTimeRepeating.MONTH -> repeatingTypeMonthOption = selectedTypeOption
                else -> {}
            }
        }
    }

    Column(
        modifier = modifier,
    ) {
        TimeAlarmTitle(
            modifier = Modifier.fillMaxWidth(),
            isChecked = isChecked,
            colorIndex = colorIndex,
            onCheckedChangedEvent = onCheckedChangedEvent
        )
        if (isChecked) {
            // 시간 반복 타입 설정
            SelectRepeatingContainer(
                modifier = Modifier.fillMaxWidth(),
                selectedType = type,
                colorIndex = colorIndex,
                onTypeClickEvent = onTypeClickEvent
            )
            Spacer(modifier = Modifier.height(15.dp))
            // 반복 옵션
            RepeatingOptionContainer(
                modifier = Modifier.fillMaxWidth(),
                selectedType = type,
            ) {
                if (type == TypeTimeRepeating.NONE) {
                    // 반복 안함 특정 날짜 선택
                    showDatePicker = true
                } else {
                    // 반복 옵션 설정 값 선택 모달 뷰 (반복 할 특정 날짜, 요일, 일)
                    showTypeOptionBottomSheet = true
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            // 반복 시간 선택
            BottomArrowSelectBox(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = com.suni.todo.R.string.str_time_hint),
            ) {
                // 반복 시간 선택 모달 뷰
                showTimeBottomSheet = true
            }
            // 날짜 선택 피커
            if (showDatePicker) {
                TdrDatePicker(
                    modifier = Modifier.fillMaxWidth(),
                    yearNow = 2024,
                    onDateSelected = {
                        showDatePicker = false
                    },
                    onDismiss = {
                        showDatePicker = false
                    },
                )
            }
            // 모달 뷰
            if (showTypeOptionBottomSheet || showTimeBottomSheet) {
                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                val coroutineScope = rememberCoroutineScope()

                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        showTimeBottomSheet = false
                        showTypeOptionBottomSheet = false
                    },
                ) {
                    if (showTimeBottomSheet) {
                        // 반복 시간 선택
                        // 시간 선택
                        TdrTimePickerContainer(
                            modifier = Modifier.fillMaxWidth()
                        ) { selectedValue ->
                            coroutineScope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                showTimeBottomSheet = false
                                showTypeOptionBottomSheet = false
                            }
                        }
                    } else if (showTypeOptionBottomSheet) {
                        // 반복 옵션 설정 값 선택 (반복 할 특정 날짜, 요일, 일)
                        SelectRepeatingTypeBottomModalContainer(
                            modifier = Modifier,
                        ) { selectedValue ->
                            coroutineScope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                showTimeBottomSheet = false
                                showTypeOptionBottomSheet = false
                            }
                        }
                    }
                }
            }

        }
    }
}

/**
 * 시간 알림 설정 타이틀
 */
@Composable
private fun TimeAlarmTitle(
    modifier: Modifier,
    isChecked: Boolean,
    colorIndex: Int,
    onCheckedChangedEvent: (checked: Boolean) -> Unit = {_ ->},
) {
    val rememberChecked = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        rememberChecked.value = isChecked
    }

    Row(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.time_alarm_title),
        )
        Switch(
            checked = rememberChecked.value,
            onCheckedChange = { checked ->
                rememberChecked.value = checked
                onCheckedChangedEvent(checked)
            }
        )
    }
}

/**
 * 시간 알림 반복 타입 설정
 */
@Composable
private fun SelectRepeatingContainer(
    modifier: Modifier,
    selectedType: TypeTimeRepeating,
    colorIndex: Int,
    onTypeClickEvent: (type: TypeTimeRepeating) -> Unit = {_ ->}
) {
    // 반복 타입 설정 탭
    Row(
        modifier = modifier.height(45.dp)
    ) {
        // 반복안함
        RepeatingTypeBox(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            type = TypeTimeRepeating.NONE,
            selectedType = selectedType,
            colorIndex = colorIndex,
            onClickEvent = onTypeClickEvent,
        )
        // 매일
        RepeatingTypeBox(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            type = TypeTimeRepeating.DAY,
            selectedType = selectedType,
            colorIndex = colorIndex,
            onClickEvent = onTypeClickEvent,
        )
        // 매주
        RepeatingTypeBox(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            type = TypeTimeRepeating.WEEK,
            selectedType = selectedType,
            colorIndex = colorIndex,
            onClickEvent = onTypeClickEvent,
        )
        // 매월
        RepeatingTypeBox(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            type = TypeTimeRepeating.MONTH,
            selectedType = selectedType,
            colorIndex = colorIndex,
            onClickEvent = onTypeClickEvent,
        )
    }
}

/**
 * 반복 타입
 * @param onClickEvent 반복 타입 선택
 */
@Composable
private fun RepeatingTypeBox(
    modifier: Modifier,
    type: TypeTimeRepeating,
    selectedType: TypeTimeRepeating,
    colorIndex: Int,
    onClickEvent: (selectedType: TypeTimeRepeating) -> Unit =  {_ -> },
) {
    val isSelected = type == selectedType

    Box(
        modifier = modifier
            .background(
                color = colorResource(
                    id = if (isSelected) endColor(colorIndex) else R.color.white
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClickEvent(type)
            },
    ) {
        Text(text = stringResource(id = type.titleStrId))
    }

}

/**
 * 반복 옵션 선택 박스
 * @param selectedOptionEvent 반복 옵션 선택 후
 */
@Composable
private fun RepeatingOptionContainer(
    modifier: Modifier,
    selectedType: TypeTimeRepeating,
    selectedOptionValue: String = "",
    selectedOptionEvent: () -> Unit = {},
) {
    val initTitleStr = selectedOptionValue.ifEmpty {
        when (selectedType) {
            TypeTimeRepeating.NONE -> "yyyyMMdd".getTimeNow().toDate()?.toFullString() ?: ""
            TypeTimeRepeating.DAY -> stringResource(id = selectedType.titleStrId)
            TypeTimeRepeating.WEEK -> stringResource(id = com.suni.todo.R.string.str_every_week_hint)
            TypeTimeRepeating.MONTH -> stringResource(id = com.suni.todo.R.string.str_every_month_hint)
        }
    }

    var rememberOptionTitle by remember { mutableStateOf("") }

    LaunchedEffect(selectedType) {
        rememberOptionTitle = initTitleStr
    }

    BottomArrowSelectBox(
        modifier = modifier,
        title = rememberOptionTitle,
        onClickEvent = selectedOptionEvent
    )
}

/**
 * 반복 옵션 모달
 */
@Composable
private fun SelectRepeatingTypeBottomModalContainer(
    modifier: Modifier,
    onTopButtonClickEvent: (selectedValue: String) -> Unit = {_ ->},
) {
    var selectedValue by remember { mutableStateOf("") }

    Column(
        modifier = modifier.height(155.dp),
    ) {
        // 시간 선택
        TdrTimePickerContainer(modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(55.dp))
        // 확인 버튼
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onTopButtonClickEvent(selectedValue)
            },
        ) {
            Text(text = "확인")
        }
    }
}