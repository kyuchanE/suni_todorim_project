package com.suni.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.suni.ui.R

/**
 * @param text
 * @param textColor
 * @param gradient
 * @param modifier
 * @param onClick
 */
@Composable
fun GradientButton(
    text: String,
    textColor: Color = colorResource(id = R.color.tdr_default),
    modifier: Modifier,
    onClick: () -> Unit = {},
    selectedColorIndex: Int = 0,
) {
    var startColor = R.color.tdr_default
    var endColor = R.color.tdr_default

    when(selectedColorIndex) {
        1 -> {
            startColor = R.color.gradient_start_1
            endColor = R.color.gradient_end_1
        }
        2 -> {
            startColor = R.color.gradient_start_2
            endColor = R.color.gradient_end_2
        }
        3 -> {
            startColor = R.color.gradient_start_3
            endColor = R.color.gradient_end_3
        }
        4 -> {
            startColor = R.color.gradient_start_4
            endColor = R.color.gradient_end_4
        }
        5 -> {
            startColor = R.color.gradient_start_5
            endColor = R.color.gradient_end_5
        }
        6 -> {
            startColor = R.color.gradient_start_6
            endColor = R.color.gradient_end_6
        }
        7 -> {
            startColor = R.color.gradient_start_7
            endColor = R.color.gradient_end_7
        }
        8 -> {
            startColor = R.color.gradient_start_8
            endColor = R.color.gradient_end_8
        }
        9 -> {
            startColor = R.color.gradient_start_9
            endColor = R.color.gradient_end_9
        }
        else -> { }
    }

    val selectedGradient = Brush.horizontalGradient(
        listOf(
            colorResource(
                id = startColor
            ),
            colorResource(
                id = endColor
            )
        )
    )

    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            contentColor = textColor,
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(15.dp),
        contentPadding = PaddingValues(),
        onClick = { onClick() },
    ) {
        Box(
            modifier = Modifier
                .background(selectedGradient)
                .then(modifier),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = text)
        }
    }
}