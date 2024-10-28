package com.loki.plitso.presentation.document.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.loki.plitso.data.local.models.FoodDocument
import com.loki.plitso.presentation.document.MealType
import java.util.Locale

@Composable
fun RecentFoodDocumentItem(
    modifier: Modifier = Modifier,
    foodDocument: FoodDocument,
) {
    val foodDoc = MealType.entries.find { it.name == foodDocument.type }

    Box(
        modifier =
            modifier
                .width(150.dp)
                .border(
                    color = MaterialTheme.colorScheme.onBackground.copy(.1f),
                    width = 1.dp,
                    shape = RoundedCornerShape(12.dp),
                ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = foodDocument.type.uppercase(Locale.ENGLISH), fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
                AsyncImage(
                    model = foodDoc?.image,
                    contentDescription = foodDoc?.name,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = foodDocument.description,
                color = MaterialTheme.colorScheme.onBackground.copy(.7f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            val dateTime = foodDocument.servedOn.split(",")
            Text(
                text = dateTime.joinToString("   "),
                color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                fontSize = 12.sp,
            )
        }
    }
}
