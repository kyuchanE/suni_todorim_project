package com.suni.todogroup.ui.create

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.suni.domain.findActivity

@Composable
fun CreateGroupScreen() {
    val context = LocalContext.current

    Scaffold { pv ->
        Surface(
            modifier = Modifier.padding(pv),
            color = Color.Magenta,
        ) {
            Row {
                Button(onClick = {
                    context.findActivity().finish()
                }) {
                    Text(text = "Group Create BACK")
                }
            }
        }

    }
}