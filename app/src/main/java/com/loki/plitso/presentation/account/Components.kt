package com.loki.plitso.presentation.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.loki.plitso.util.noIndication

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    itemList: List<SettingItem>,
    onItemClick: (itemIndex: Int) -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
        ) {
            itemList.forEachIndexed { index, item ->
                Item(
                    settingItem = item,
                    modifier = Modifier.padding(vertical = 8.dp),
                    onItemClick = { onItemClick(index) },
                )
            }
        }
    }
}

@Composable
private fun Item(
    modifier: Modifier = Modifier,
    settingItem: SettingItem,
    onItemClick: () -> Unit,
) {
    Box(
        modifier = Modifier.noIndication { onItemClick() },
    ) {
        Row(
            modifier =
                modifier
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (settingItem.icon != null || settingItem.imageUrl != null) {
                Box(
                    modifier =
                        Modifier
                            .size(40.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = CircleShape,
                            ).clip(CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    settingItem.icon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = settingItem.content,
                            modifier = Modifier.size(25.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(.5f),
                        )
                    }

                    settingItem.imageUrl?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = settingItem.content,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))
            }

            Column {
                Text(text = settingItem.content!!, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                settingItem.subContent?.let {
                    Spacer(modifier = Modifier.height(2.dp))
                    it()
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            settingItem.switchData?.let { data ->
                Switch(
                    checked = data.isChecked,
                    onCheckedChange = data.onCheckedChange,
                )
            }
        }
    }
}

data class SettingItem(
    val icon: ImageVector? = null,
    val imageUrl: String? = null,
    val content: String? = null,
    val subContent: (@Composable () -> Unit)? = null,
    val switchData: SwitchData? = null,
)

data class SwitchData(
    val isChecked: Boolean,
    val onCheckedChange: (Boolean) -> Unit,
)
