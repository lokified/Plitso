package com.loki.plitso.presentation.document.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatAlignLeft
import androidx.compose.material.icons.automirrored.outlined.FormatAlignRight
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.FormatAlignCenter
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.material.icons.outlined.FormatItalic
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.FormatStrikethrough
import androidx.compose.material.icons.outlined.FormatUnderlined
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loki.plitso.util.noIndication
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun EditorPanelButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    tint: Color? = null,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .focusProperties { canFocus = false }
            .clip(RoundedCornerShape(10.dp))
            .noIndication { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = icon.name,
            tint = tint ?: Color(0xFFCBCCCD),
            modifier = Modifier
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(.1f)
                    else Color.Transparent,
                )
                .padding(6.dp)
        )
    }
}


@Composable
fun EditorPanel(
    state: RichTextState,
    onOpenDialog: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
    ) {
        item {
            EditorPanelButton(
                onClick = {
                    state.addParagraphStyle(
                        ParagraphStyle(
                            textAlign = TextAlign.Left,
                        )
                    )
                },
                isSelected = state.currentParagraphStyle.textAlign == TextAlign.Left,
                icon = Icons.AutoMirrored.Outlined.FormatAlignLeft
            )
        }

        item {
            EditorPanelButton(
                onClick = {
                    state.addParagraphStyle(
                        ParagraphStyle(
                            textAlign = TextAlign.Center
                        )
                    )
                },
                isSelected = state.currentParagraphStyle.textAlign == TextAlign.Center,
                icon = Icons.Outlined.FormatAlignCenter
            )
        }

        item {
            EditorPanelButton(
                onClick = {
                    state.addParagraphStyle(
                        ParagraphStyle(
                            textAlign = TextAlign.Right
                        )
                    )
                },
                isSelected = state.currentParagraphStyle.textAlign == TextAlign.Right,
                icon = Icons.AutoMirrored.Outlined.FormatAlignRight
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }

        item {
            EditorPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontWeight == FontWeight.Bold,
                icon = Icons.Outlined.FormatBold
            )
        }

        item {
            EditorPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontStyle = FontStyle.Italic
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontStyle == FontStyle.Italic,
                icon = Icons.Outlined.FormatItalic
            )
        }

        item {
            EditorPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                },
                isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline) == true,
                icon = Icons.Outlined.FormatUnderlined
            )
        }

        item {
            EditorPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                },
                isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.LineThrough) == true,
                icon = Icons.Outlined.FormatStrikethrough
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }

        item {
            EditorPanelButton(
                onClick = {
                    onOpenDialog(true)
                },
                isSelected = state.isLink,
                icon = Icons.Outlined.Link
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }

        item {
            EditorPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontSize = 28.sp
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontSize == 28.sp,
                icon = Icons.Outlined.FormatSize
            )
        }

        item {
            EditorPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            color = Color.Red
                        )
                    )
                },
                isSelected = state.currentSpanStyle.color == Color.Red,
                icon = Icons.Filled.Circle,
                tint = Color.Red
            )
        }

        item {
            EditorPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            background = Color.Yellow
                        )
                    )
                },
                isSelected = state.currentSpanStyle.background == Color.Yellow,
                icon = Icons.Outlined.Circle,
                tint = Color.Yellow
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }

        item {
            EditorPanelButton(
                onClick = {
                    state.toggleUnorderedList()
                },
                isSelected = state.isUnorderedList,
                icon = Icons.AutoMirrored.Outlined.FormatListBulleted,
            )
        }

        item {
            EditorPanelButton(
                onClick = {
                    state.toggleOrderedList()
                },
                isSelected = state.isOrderedList,
                icon = Icons.Outlined.FormatListNumbered,
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }

        item {
            EditorPanelButton(
                onClick = {
                    state.toggleCodeSpan()
                },
                isSelected = state.isCodeSpan,
                icon = Icons.Outlined.Code,
            )
        }
    }
}

@Composable
fun EditorLinkDialog(
    state: RichTextState,
    onDismiss: (Boolean) -> Unit,
) {
    var text by remember { mutableStateOf(state.selectedLinkText.orEmpty()) }
    var link by remember { mutableStateOf(state.selectedLinkUrl.orEmpty()) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF1a1d21))
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Add link",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    onDismiss(false)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value =
            if (state.selection.collapsed)
                text
            else
                state.annotatedString.text.substring(
                    state.selection.min,
                    state.selection.max
                ),
            onValueChange = {
                text = it
            },
            label = {
                Text(
                    text = "Text",
                    color = Color.White
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White
            ),
            enabled = state.selection.collapsed && !state.isLink,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = link,
            onValueChange = {
                link = it
            },
            label = {
                Text(
                    text = "Link",
                    color = Color.White
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.End)
        ) {
            if (state.isLink) {
                OutlinedButton(
                    onClick = {
                        state.removeLink()
                        onDismiss(false)
                        text = ""
                        link = ""
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.Red
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                ) {
                    Text(
                        text = "Remove",
                        color = Color.Red
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))
            }

            TextButton(
                onClick = {
                    onDismiss(false)
                    text = ""
                    link = ""
                },
                modifier = Modifier
            ) {
                Text(
                    text = "Cancel"
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            TextButton(
                onClick = {
                    when {
                        state.isLink ->
                            state.updateLink(
                                url = link,
                            )

                        state.selection.collapsed ->
                            state.addLink(
                                text = text,
                                url = link
                            )

                        else ->
                            state.addLinkToSelection(
                                url = link
                            )
                    }

                    onDismiss(false)
                    text = ""
                    link = ""
                },
                enabled = (text.isNotEmpty() || !state.selection.collapsed) && link.isNotEmpty(),
            ) {
                Text(
                    text = "Save"
                )
            }
        }
    }
}