package com.loki.plitso.presentation.document

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loki.plitso.R
import com.loki.plitso.data.local.models.FoodDocument
import com.loki.plitso.presentation.document.components.BoxDropdownInput
import com.loki.plitso.presentation.document.components.BoxInput
import com.loki.plitso.presentation.document.components.RecentFoodDocumentItem
import com.loki.plitso.presentation.document.components.textFieldColors
import com.loki.plitso.util.TimeUtil.convertMillisToDate
import com.loki.plitso.util.TimeUtil.currentDate
import com.loki.plitso.util.TimeUtil.currentTime
import com.loki.plitso.util.noIndication
import com.loki.plitso.util.showToast

enum class MealType(
    @DrawableRes val image: Int,
) {
    BREAKFAST(R.drawable.breakfast),
    LUNCH(R.drawable.lunch),
    DINNER(R.drawable.dinner),
}

@Composable
fun DocumentScreen(
    foodDocuments: List<FoodDocument>,
    documentViewModel: DocumentViewModel,
    navigateToAllFoodDocument: () -> Unit,
) {
    val context = LocalContext.current
    val foodDocument by documentViewModel.foodDocument.collectAsStateWithLifecycle()
    var containerState by remember { mutableStateOf(ContainerState.BOX_INPUT) }

    LaunchedEffect(
        documentViewModel.error.value,
    ) {
        if (documentViewModel.error.value.isNotEmpty()) {
            context.showToast(documentViewModel.error.value)
        }
    }

    if (containerState == ContainerState.EDITOR_INPUT) {
        BackHandler {
            containerState = ContainerState.BOX_INPUT
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text =
                        if (containerState ==
                            ContainerState.EDITOR_INPUT
                        ) {
                            "Editor"
                        } else {
                            "Document"
                        },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )

                if (containerState == ContainerState.EDITOR_INPUT) {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            containerState =
                                if (containerState ==
                                    ContainerState.EDITOR_INPUT
                                ) {
                                    ContainerState.BOX_INPUT
                                } else {
                                    ContainerState.EDITOR_INPUT
                                }
                        },
                        modifier =
                            Modifier
                                .padding(8.dp),
                    ) {
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                    }
                }
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
            ) {
                if (containerState == ContainerState.BOX_INPUT) {
                    TopSection(
                        foodDocuments = foodDocuments,
                        navigateToAllFoodDocument = navigateToAllFoodDocument,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                DocumentSection(
                    documentViewModel = documentViewModel,
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                    containerState = containerState,
                    selectedContainer = {
                        containerState = it
                    },
                )
            }
        }

        if (containerState == ContainerState.BOX_INPUT) {
            Box(
                modifier =
                    Modifier
                        .padding(bottom = 20.dp, end = 24.dp)
                        .align(Alignment.BottomEnd)
                        .clip(RoundedCornerShape(10.dp))
                        .noIndication(
                            enabled = foodDocument.description.isNotEmpty(),
                        ) {
                            if (documentViewModel.error.value.isEmpty()) {
                                documentViewModel.saveFoodDocument()
                            }
                        },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkAdd,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier =
                        Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (foodDocument.description.isEmpty()) {
                                    MaterialTheme.colorScheme.primary.copy(.4f)
                                } else {
                                    MaterialTheme.colorScheme.primary
                                },
                            ).padding(6.dp),
                )
            }
        }
    }
}

@Composable
fun TopSection(
    modifier: Modifier = Modifier,
    foodDocuments: List<FoodDocument>,
    navigateToAllFoodDocument: () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (foodDocuments.isEmpty()) {
                Text(
                    text = "No recent food taken",
                    color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                )
            } else {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = "Recently Eaten",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = navigateToAllFoodDocument) {
                            Text(text = "View All")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowRightAlt,
                                contentDescription = null,
                            )
                        }
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                    ) {
                        val foodList =
                            if (foodDocuments.size >= 5) foodDocuments.take(5) else foodDocuments
                        items(foodList) { foodDocument ->
                            RecentFoodDocumentItem(foodDocument = foodDocument)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DocumentSection(
    modifier: Modifier = Modifier,
    documentViewModel: DocumentViewModel,
    containerState: ContainerState,
    selectedContainer: (ContainerState) -> Unit,
) {
    val foodDocument by documentViewModel.foodDocument.collectAsStateWithLifecycle()

    Column(
        modifier = modifier,
    ) {
        Text(text = "Document your food cycle", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "What?",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Thin,
        )
        Spacer(modifier = Modifier.height(12.dp))
        BoxDropdownInput(
            selectedOption = foodDocument.type,
            onChangeOption = documentViewModel::onTypeChange,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "When?",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Thin,
        )
        Spacer(modifier = Modifier.height(12.dp))
        TimePickerDialog(documentViewModel = documentViewModel)
        Spacer(modifier = Modifier.height(8.dp))
        DatePickerDialog(documentViewModel = documentViewModel)
        Spacer(modifier = Modifier.height(24.dp))

        EditorContainer(
            containerState = containerState,
            documentViewModel = documentViewModel,
            selectedContainer = selectedContainer,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    modifier: Modifier = Modifier,
    documentViewModel: DocumentViewModel,
) {
    val time by documentViewModel.time.collectAsStateWithLifecycle()
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(is24Hour = true)

    BoxInput(
        value = time,
        onValueChange = documentViewModel::onTimeChange,
        modifier =
            modifier.noIndication {
                showTimePicker = true
            },
        icon = R.drawable.time,
        placeholder = "Pick Time",
    )

    if (showTimePicker) {
        BasicAlertDialog(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .clip(RoundedCornerShape(12.dp)),
            onDismissRequest = { showTimePicker = false },
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(12.dp),
            ) {
                TimePicker(
                    state = timePickerState,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier
                            .align(Alignment.End),
                ) {
                    TextButton(onClick = {
                        showTimePicker = false
                    }) {
                        Text(text = "Cancel")
                    }

                    TextButton(
                        onClick = {
                            showTimePicker = false
                            val hour =
                                if (timePickerState.hour.toString().length ==
                                    1
                                ) {
                                    "0${timePickerState.hour}"
                                } else {
                                    timePickerState.hour
                                }
                            val minute =
                                if (timePickerState.minute ==
                                    0
                                ) {
                                    "${timePickerState.minute}0"
                                } else {
                                    timePickerState.minute
                                }
                            val selectedTime =
                                if (timePickerState.hour == 0) currentTime() else "$hour:$minute"

                            documentViewModel.onTimeChange(selectedTime)
                        },
                    ) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    modifier: Modifier = Modifier,
    documentViewModel: DocumentViewModel,
) {
    val date by documentViewModel.date.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState =
        rememberDatePickerState(
            selectableDates =
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                        utcTimeMillis <= System.currentTimeMillis()
                },
        )

    BoxInput(
        value = date,
        onValueChange = documentViewModel::onDateChange,
        modifier =
            modifier.noIndication {
                showDatePicker = true
            },
        icon = R.drawable.cal,
        placeholder = "Pick Date",
    )

    if (showDatePicker) {
        BasicAlertDialog(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .clip(RoundedCornerShape(12.dp)),
            onDismissRequest = { showDatePicker = false },
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(12.dp),
            ) {
                DatePicker(
                    state = datePickerState,
                    colors =
                        DatePickerDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier
                            .align(Alignment.End),
                ) {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(text = "Cancel")
                    }

                    TextButton(
                        onClick = {
                            showDatePicker = false
                            val selectedDate =
                                datePickerState.selectedDateMillis?.let {
                                    convertMillisToDate(it)
                                } ?: currentDate()
                            documentViewModel.onDateChange(selectedDate)
                        },
                    ) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}

enum class ContainerState {
    BOX_INPUT,
    EDITOR_INPUT,
}

@Composable
private fun EditorContainer(
    modifier: Modifier = Modifier,
    containerState: ContainerState,
    documentViewModel: DocumentViewModel,
    selectedContainer: (ContainerState) -> Unit,
) {
    val foodDocument by documentViewModel.foodDocument.collectAsStateWithLifecycle()
    val transition = updateTransition(targetState = containerState, label = "transition")

    val cornerRadius by transition.animateDp(
        label = "corner radius",
        transitionSpec = {
            when (targetState) {
                ContainerState.BOX_INPUT ->
                    tween(
                        durationMillis = 200,
                        easing = EaseOutCubic,
                    )

                ContainerState.EDITOR_INPUT ->
                    tween(
                        durationMillis = 200,
                        easing = FastOutSlowInEasing,
                    )
            }
        },
    ) { state ->
        when (state) {
            ContainerState.EDITOR_INPUT -> 0.dp
            ContainerState.BOX_INPUT -> 12.dp
        }
    }

    val backgroundColor by transition.animateColor(label = "background color") { state ->
        when (state) {
            ContainerState.BOX_INPUT -> MaterialTheme.colorScheme.surface.copy(.5f)
            ContainerState.EDITOR_INPUT -> MaterialTheme.colorScheme.background
        }
    }

    transition.AnimatedContent(
        modifier =
            modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(cornerRadius))
                .drawBehind {
                    drawRect(color = backgroundColor)
                },
    ) { state ->
        when (state) {
            ContainerState.BOX_INPUT -> {
                DocumentInput(
                    foodDocument = foodDocument,
                    onChangePreview = {
                        selectedContainer(ContainerState.EDITOR_INPUT)
                    },
                )
            }

            ContainerState.EDITOR_INPUT -> {
                EditorBox(
                    value = foodDocument.description,
                    onValueChange = documentViewModel::onDescriptionChange,
                    modifier = Modifier.imePadding(),
                )
            }
        }
    }
}

@Composable
fun DocumentInput(
    modifier: Modifier = Modifier,
    foodDocument: FoodDocument,
    onChangePreview: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .noIndication { onChangePreview() },
    ) {
        Text(
            text = foodDocument.description.ifEmpty { "Document your meal here" },
            color = MaterialTheme.colorScheme.onBackground.copy(.5f),
            modifier = Modifier.padding(12.dp),
        )
    }
}

@Composable
fun EditorBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onBackground.copy(.05f),
                    RoundedCornerShape(12.dp),
                ),
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = "Document your meal eg. ingredients",
                )
            },
            colors = textFieldColors(),
            modifier =
                Modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester),
        )
    }
}
