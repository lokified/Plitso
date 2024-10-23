package com.loki.plitso.presentation.foodDocuments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.loki.plitso.data.local.models.FoodDocument
import com.loki.plitso.presentation.document.DocumentViewModel
import com.loki.plitso.presentation.document.MealType
import com.loki.plitso.util.TimeUtil
import com.loki.plitso.util.noIndication
import com.loki.plitso.util.toRichHtmlString

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodDocumentsScreen(
    foodDocuments: List<FoodDocument>,
    documentViewModel: DocumentViewModel,
    navigateBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "My Meals",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val groupedTime =
                foodDocuments.groupBy {
                    val dateTime = TimeUtil.formatDateTime(it.servedOn).split(",")
                    dateTime[0]
                }

            for ((time, documents) in groupedTime) {
                stickyHeader {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background),
                    ) {
                        Text(
                            text = time,
                            modifier = Modifier.padding(vertical = 4.dp),
                        )
                    }
                }

                items(documents) { foodDocument ->
                    val dateTime = TimeUtil.formatDateTime(foodDocument.servedOn).split(",")

                    FoodDocumentItem(
                        dateTime = dateTime[1],
                        foodDocument = foodDocument,
                        onItemDelete = { documentViewModel.deleteFoodDocument(foodDocument) },
                        onItemClick = {},
                    )
                }
            }
        }
    }
}

@Composable
fun FoodDocumentItem(
    modifier: Modifier = Modifier,
    foodDocument: FoodDocument,
    dateTime: String,
    onItemDelete: () -> Unit,
    onItemClick: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surface.copy(.1f),
                    shape = RoundedCornerShape(8.dp),
                ).background(MaterialTheme.colorScheme.surface.copy(.5f))
                .noIndication { onItemClick() },
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
                AsyncImage(
                    model = MealType.entries.find { it.name == foodDocument.type }?.image,
                    contentDescription = "food type",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = foodDocument.type, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onItemDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = foodDocument.description.toRichHtmlString(),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground.copy(.6f),
                modifier = Modifier.padding(8.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = dateTime,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(.4f),
            )
        }
    }
}
