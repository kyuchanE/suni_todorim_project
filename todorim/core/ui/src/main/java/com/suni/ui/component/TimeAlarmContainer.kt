package com.suni.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suni.ui.R

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
@Composable
fun TimeAlarmContainer(
    modifier: Modifier,
    isChecked: Boolean = false,
    type: TypeTimeRepeating = TypeTimeRepeating.NONE,
    selectedTime: String = "",
    colorIndex: Int,
    onCheckedChangedEvent: (checked: Boolean) -> Unit = {_ ->},
    onTypeClickEvent: (type: TypeTimeRepeating) -> Unit = {_ ->}
) {
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
            SelectRepeatingContainer(
                modifier = Modifier.fillMaxWidth(),
                selectedType = type,
                selectedTime = selectedTime,
                colorIndex = colorIndex,
                onTypeClickEvent = onTypeClickEvent
            )
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
 * 시간 알림 데이터 설정
 */
@Composable
private fun SelectRepeatingContainer(
    modifier: Modifier,
    selectedType: TypeTimeRepeating,
    selectedTime: String = "",
    colorIndex: Int,
    onTypeClickEvent: (type: TypeTimeRepeating) -> Unit = {_ ->}
) {
    Column(
        modifier = modifier,
    ) {
        // 반복 타입 설정 탭
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
        ) {
            // 반복안함
            RepeatingTypeBox(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                type = TypeTimeRepeating.NONE,
                selectedType = selectedType,
                colorIndex = colorIndex,
                onClickEvent = onTypeClickEvent,
            )
            // 매일
            RepeatingTypeBox(
                modifier = Modifier.weight(1f),
                type = TypeTimeRepeating.DAY,
                selectedType = selectedType,
                colorIndex = colorIndex,
                onClickEvent = onTypeClickEvent,
            )
            // 매주
            RepeatingTypeBox(
                modifier = Modifier.weight(1f),
                type = TypeTimeRepeating.WEEK,
                selectedType = selectedType,
                colorIndex = colorIndex,
                onClickEvent = onTypeClickEvent,
            )
            // 매월
            RepeatingTypeBox(
                modifier = Modifier.weight(1f),
                type = TypeTimeRepeating.MONTH,
                selectedType = selectedType,
                colorIndex = colorIndex,
                onClickEvent = onTypeClickEvent,
            )
        }
        // 반복 세부 설정
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

