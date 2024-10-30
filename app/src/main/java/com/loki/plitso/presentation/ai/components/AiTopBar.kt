package com.loki.plitso.presentation.ai.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loki.plitso.R

@Composable
fun AiTopBar(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.ask_ai),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
        )

        IconButton(
            onClick = navigateBack,
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
            )
        }
    }
}
