package com.suni.todorim.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suni.data.model.TodoEntity
import com.suni.ui.component.LinearGradientProgressIndicator
import com.suni.ui.component.TdrOnlyCheckBox

/**
 * 그룹 상세 카드
 * @param modifier: Modifier
 * @param title: String : 그룹 이름
 * @param colorIndex: Int : 색상 인덱스
 * @param todoCompletedPercent: Float : 할 일 완료 퍼센트
 * @param todoItemList: MutableList<TodoEntity> : 할 일 목록
 * @param onClickDetailCard: () -> Unit : 카드 클릭 이벤트
 * @param onClickCheckBox: (result: TodoEntity) -> Unit : 체크박스 클릭 이벤트
 */
@Composable
fun GroupDetailCard(
    modifier: Modifier,
    title: String,
    colorIndex: Int,
    todoCompletedPercent: Float,
    todoItemList: MutableList<TodoEntity>,
    onClickDetailCard: () -> Unit,
    onClickCheckBox: (result: TodoEntity) -> Unit,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(Color.White),
        onClick = onClickDetailCard
    ) {
        // Group Title
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(com.suni.ui.R.color.tdr_default),
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
        )
        Spacer(modifier = Modifier.height(15.dp))
        // 할 일 진행률
        LinearGradientProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp),
            backgroundColor = Color.LightGray,
            colorIndex = colorIndex,
            percent = todoCompletedPercent,
        )
        Spacer(modifier = Modifier.height(25.dp))
        // 할 일 목록
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            userScrollEnabled = false,
        ) {
            todoItemList.sortBy { it.todoId }
            itemsIndexed(
                items = todoItemList,
                key = { index, todoEntity ->
                    todoEntity.todoId
                },
            ) { _, todoEntity ->
                TdrOnlyCheckBox(
                    modifier = Modifier.fillMaxWidth(),
                    todoTitle = todoEntity.title,
                    isSelected = todoEntity.isCompleted,
                ) { isSelected ->
                    val resultItem = TodoEntity().apply {
                        this.todoId = todoEntity.todoId
                        this.groupId = todoEntity.groupId
                        this.isCompleted = isSelected
                        this.title = todoEntity.title
                        this.order = todoEntity.order
                    }
                    onClickCheckBox(resultItem)
                }
            }
        }
    }
}