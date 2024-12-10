package com.suni.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.suni.ui.R

@Composable
fun BottomArrowSelectBox(
    modifier: Modifier,
    title: String,
    strHint: String = "",
    onClickEvent: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RectangleShape,
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClickEvent
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = if (title == strHint) {
                    Color.LightGray
                } else {
                    colorResource(id = R.color.tdr_default)
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Icon(
                modifier = Modifier.size(35.dp),
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                tint = Color.LightGray,
                contentDescription = "down arrow"
            )
        }
    }
}