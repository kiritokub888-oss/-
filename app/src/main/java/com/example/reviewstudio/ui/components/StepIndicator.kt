package com.example.reviewstudio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reviewstudio.model.ReviewStudioStep
import com.example.reviewstudio.ui.theme.AccentOrange
import com.example.reviewstudio.ui.theme.BorderColor
import com.example.reviewstudio.ui.theme.DarkSurface
import com.example.reviewstudio.ui.theme.SuccessGreen
import com.example.reviewstudio.ui.theme.TextPrimary
import com.example.reviewstudio.ui.theme.TextSecondary

@Composable
fun StepIndicator(
    currentStep: ReviewStudioStep,
    onStepClick: (ReviewStudioStep) -> Unit,
    canNavigateTo: (ReviewStudioStep) -> Boolean
) {
    val steps = listOf(
        ReviewStudioStep.PRODUCT to "1. สินค้า",
        ReviewStudioStep.SCRIPT to "2. สคริปต์",
        ReviewStudioStep.AVATAR to "3. Avatar",
        ReviewStudioStep.RENDERING to "4. วิดีโอ"
    )

    val currentIndex = when (currentStep) {
        ReviewStudioStep.PRODUCT -> 0
        ReviewStudioStep.SCRIPT -> 1
        ReviewStudioStep.AVATAR -> 2
        ReviewStudioStep.RENDERING, ReviewStudioStep.RESULT -> 3
    }

    Surface(
        color = DarkSurface,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("step_indicator")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            steps.forEachIndexed { index, (step, label) ->
                val isCurrent = index == currentIndex
                val isCompleted = index < currentIndex
                val isEnabled = canNavigateTo(step)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 3.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isCurrent) Color(0xFF1E293B) else Color.Transparent
                        )
                        .clickable(enabled = isEnabled) { onStepClick(step) }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCompleted -> SuccessGreen
                                        isCurrent -> AccentOrange
                                        else -> Color(0xFF334155)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Completed",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            } else {
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            color = when {
                                isCurrent -> AccentOrange
                                isCompleted -> TextPrimary
                                else -> TextSecondary
                            },
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
