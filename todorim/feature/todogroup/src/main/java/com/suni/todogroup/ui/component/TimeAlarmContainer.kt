package com.suni.todogroup.ui.component

import androidx.compose.animation.AnimatedContent
import com.suni.ui.component.endColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.suni.domain.convertToStrMonth
import com.suni.domain.convertToStrWeek
import com.suni.domain.getTimeNow
import com.suni.domain.toCommonTypeString
import com.suni.domain.toDate
import com.suni.domain.toFullString
import com.suni.ui.R
import com.suni.ui.component.BottomArrowSelectBox
import com.suni.ui.component.SelectOnePickerType
import com.suni.ui.component.TdrDatePicker
import com.suni.ui.component.TdrSelectOnePickerContainer
import com.suni.ui.component.TdrTimePicker
import kotlinx.coroutines.launch

enum class TypeTimeRepeating(val titleStrId: Int) {
    NONE(R.string.time_repeating_none),
    DAY(R.string.time_repeating_every_day),
    WEEK(R.string.time_repeating_every_week),
    MONTH(R.string.time_repeating_every_month),
}

/**
 * 특정 시간 알림 설정
 * @param onCheckedChangedEvent 시간 알림 설정
 * @param onTypeClickEvent 반복 알림 설정 
 * @param selectedTypeOption 반복 옵션 선택 (특정날짜, 요일반복, 월 반복)
 * @param selectedTimeOptionEvent 시간 반복 옵션 선택 (시간 반복)
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
    onTypeClickEvent: (type: TypeTimeRepeating) -> Unit = {_ ->},
    selectedTypeOptionEvent: (typeOption: String) -> Unit = {_->},
    selectedTimeOptionEvent: (timeOption: String) -> Unit = {_->},      // HH:mm 형태로 전달
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
        Spacer(modifier = Modifier.height(10.dp))
        // 시간 알림 선택 옵션 (애니메이션)
        AnimatedContent(
            targetState = isChecked,
            label = "Animation TimeAlarmContainer",
        ) { targetState ->
            if (targetState) {
                Column {
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        selectedType = type,
                        selectedOptionValue =
                        if(type == TypeTimeRepeating.NONE) {
                            selectedTypeOption.toDate("yyyy-MM-dd")?.toFullString() ?: ""
                        } else { selectedTypeOption },
                    ) {
                        when(type) {
                            TypeTimeRepeating.NONE -> {
                                // 반복 안함 특정 날짜 선택
                                showDatePicker = true
                            }
                            TypeTimeRepeating.DAY -> {
                                // 매일
                            }
                            else -> {
                                // 반복 옵션 설정 값 선택 모달 뷰 (반복 할 특정 날짜, 요일, 일)
                                showTypeOptionBottomSheet = true
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    // 반복 시간 선택
                    BottomArrowSelectBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        title = selectedTimeOption.ifEmpty {
                            stringResource(id = com.suni.todogroup.R.string.str_time_hint)
                        },
                        strHint = stringResource(id = com.suni.todogroup.R.string.str_time_hint),
                    ) {
                        // 반복 시간 선택 모달 뷰
                        showTimeBottomSheet = true
                    }
                    // 날짜 선택 피커
                    if (showDatePicker) {
                        TdrDatePicker(
                            modifier = Modifier.fillMaxWidth(),
                            yearNow = 2024,
                            onDateSelected = { date ->
                                showDatePicker = false
                                selectedTypeOptionEvent(date.toCommonTypeString() ?: "")
                            },
                            onDismiss = {
                                showDatePicker = false
                            },
                        )
                    }
                    // 모달 뷰 - 반복 옵션 설정 값 선택 (반복 할 특정 날짜, 요일, 일)
                    if (showTypeOptionBottomSheet) {
                        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                        val coroutineScope = rememberCoroutineScope()

                        ModalBottomSheet(
                            sheetState = sheetState,
                            onDismissRequest = {
                                showTypeOptionBottomSheet = false
                            },
                        ) {
                            if (showTypeOptionBottomSheet) {
                                // 반복 옵션 설정 값 선택 (반복 할 특정 날짜, 요일, 일)
                                val pickerType =
                                    if (type == TypeTimeRepeating.WEEK)
                                        SelectOnePickerType.DAY_OF_WEEK
                                    else SelectOnePickerType.DAY

                                TdrSelectOnePickerContainer(
                                    modifier = Modifier.fillMaxWidth(),
                                    type = pickerType
                                ) { selectedValue ->
                                    coroutineScope.launch {
                                        sheetState.hide()
                                        selectedTypeOptionEvent(selectedValue.toString())
                                    }.invokeOnCompletion {
                                        showTypeOptionBottomSheet = false
                                    }
                                }
                            }
                        }
                    }

                    if (showTimeBottomSheet) {
                        // 반복 시간 선택
                        TdrTimePicker(
                            onConfirm = { timePickerState ->
                                // HH:mm String 형태로 보내야 함
                                selectedTimeOptionEvent(
                                    "${timePickerState.hour}:${timePickerState.minute}"
                                )
                                showTimeBottomSheet = false
                            },
                            onDismiss = { showTimeBottomSheet = false }
                        )
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
    var rememberChecked by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        rememberChecked = isChecked
    }

    Row(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.time_alarm_title),
        )
        Switch(
            checked = rememberChecked,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colorResource(id = endColor(colorIndex)),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
                uncheckedBorderColor = Color.LightGray,
            ),
            onCheckedChange = { checked ->
                rememberChecked = checked
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
        modifier = modifier
            .height(55.dp)
            .border(width = 1.dp, color = Color.LightGray, shape = RectangleShape)
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
        RepeatingTypeBoxDivider()
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
        RepeatingTypeBoxDivider()
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
        RepeatingTypeBoxDivider()
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
                ),
                shape = RectangleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClickEvent(type)
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(id = type.titleStrId),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(
                id = if (isSelected) R.color.white else R.color.tdr_default
            ),
            overflow = TextOverflow.Ellipsis,
        )
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
    val initTitleStr = if(selectedOptionValue.isEmpty()) {
        when (selectedType) {
            TypeTimeRepeating.NONE -> "yyyyMMdd".getTimeNow().toDate()?.toFullString() ?: ""
            TypeTimeRepeating.DAY -> stringResource(id = selectedType.titleStrId)
            TypeTimeRepeating.WEEK -> stringResource(id = com.suni.todogroup.R.string.str_every_week_hint)
            TypeTimeRepeating.MONTH -> stringResource(id = com.suni.todogroup.R.string.str_every_month_hint)
        }
    } else {
        when (selectedType) {
            TypeTimeRepeating.NONE,
            TypeTimeRepeating.DAY -> selectedOptionValue
            TypeTimeRepeating.WEEK -> selectedOptionValue.convertToStrWeek()
            TypeTimeRepeating.MONTH -> selectedOptionValue.convertToStrMonth()
        }
    }

    val strHint = when (selectedType) {
        TypeTimeRepeating.WEEK -> stringResource(id = com.suni.todogroup.R.string.str_every_week_hint)
        TypeTimeRepeating.MONTH -> stringResource(id = com.suni.todogroup.R.string.str_every_month_hint)
        else -> { "" }
    }

    var rememberOptionTitle by remember { mutableStateOf("") }

    LaunchedEffect(selectedType, selectedOptionValue) {
        rememberOptionTitle = initTitleStr
    }

    BottomArrowSelectBox(
        modifier = modifier,
        title = rememberOptionTitle,
        strHint = strHint,
        onClickEvent = selectedOptionEvent
    )
}

@Composable
private fun RepeatingTypeBoxDivider() {
    VerticalDivider(
        modifier = Modifier
            .width(1.dp)
            .fillMaxHeight(),
        thickness = 1.dp,
        color = Color.LightGray
    )
}
