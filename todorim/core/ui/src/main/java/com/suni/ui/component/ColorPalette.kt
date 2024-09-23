package com.suni.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suni.ui.R

/**
 * 테마 색상 팔레트
 * @param title (선택)
 * @param modifier
 * @param selectColorEvent
 * @param selectedIndex
 */
@Composable
fun ColorPalette(
    title: String = stringResource(id = R.string.default_palette_title),
    modifier: Modifier,
    selectColorEvent: (index: Int) -> Unit = { _ -> },
    selectedIndex: Int = 0,
) {
    Column(
        modifier = modifier
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CircleGradientVertical(
                startColor = R.color.tdr_default,
                endColor = R.color.tdr_default,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 1.5.dp,
                        color = colorResource(
                            id = if (selectedIndex == 0) R.color.tdr_default
                            else R.color.white
                        ),
                        shape = CircleShape
                    )
            ) {
                selectColorEvent(0)
            }
            CircleGradientVertical(
                startColor = R.color.gradient_start_1,
                endColor = R.color.gradient_end_1,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 1.5.dp,
                        color = colorResource(
                            id = if (selectedIndex == 1) R.color.tdr_default
                            else R.color.white
                        ),
                        shape = CircleShape
                    )
            ) {
                selectColorEvent(1)
            }
            CircleGradientVertical(
                startColor = R.color.gradient_start_2,
                endColor = R.color.gradient_end_2,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 1.5.dp,
                        color = colorResource(
                            id = if (selectedIndex == 2) R.color.tdr_default
                            else R.color.white
                        ),
                        shape = CircleShape
                    )
            ) {
                selectColorEvent(2)
            }
            CircleGradientVertical(
                startColor = R.color.gradient_start_3,
                endColor = R.color.gradient_end_3,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 1.5.dp,
                        color = colorResource(
                            id = if (selectedIndex == 3) R.color.tdr_default
                            else R.color.white
                        ),
                        shape = CircleShape
                    )
            ) {
                selectColorEvent(3)
            }
            CircleGradientVertical(
                startColor = R.color.gradient_start_4,
                endColor = R.color.gradient_end_4,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 1.5.dp,
                        color = colorResource(
                            id = if (selectedIndex == 4) R.color.tdr_default
                            else R.color.white
                        ),
                        shape = CircleShape
                    )
            ) {
                selectColorEvent(4)
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CircleGradientVertical(
                startColor = R.color.gradient_start_5,
                endColor = R.color.gradient_end_5,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 1.5.dp,
                        color = colorResource(
                            id = if (selectedIndex == 5) R.color.tdr_default
                            else R.color.white
                        ),
                        shape = CircleShape
                    )
            ) {
                selectColorEvent(5)
            }
            CircleGradientVertical(
                startColor = R.color.gradient_start_6,
                endColor = R.color.gradient_end_6,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 1.5.dp,
                        color = colorResource(
                            id = if (selectedIndex == 6) R.color.tdr_default
                            else R.color.white
                        ),
                        shape = CircleShape
                    )
            ) {
                selectColorEvent(6)
            }
            CircleGradientVertical(
                startColor = R.color.gradient_start_7,
                endColor = R.color.gradient_end_7,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 1.5.dp,
                        color = colorResource(
                            id = if (selectedIndex == 7) R.color.tdr_default
                            else R.color.white
                        ),
                        shape = CircleShape
                    )
            ) {
                selectColorEvent(7)
            }
            CircleGradientVertical(
                startColor = R.color.gradient_start_8,
                endColor = R.color.gradient_end_8,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 1.5.dp,
                        color = colorResource(
                            id = if (selectedIndex == 8) R.color.tdr_default
                            else R.color.white
                        ),
                        shape = CircleShape
                    )
            ) {
                selectColorEvent(8)
            }
            CircleGradientVertical(
                startColor = R.color.gradient_start_9,
                endColor = R.color.gradient_end_9,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 1.5.dp,
                        color = colorResource(
                            id = if (selectedIndex == 9) R.color.tdr_default
                            else R.color.white
                        ),
                        shape = CircleShape
                    )
            ) {
                selectColorEvent(9)
            }
        }
    }
}