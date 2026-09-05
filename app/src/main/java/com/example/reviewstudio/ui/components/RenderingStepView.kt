package com.example.reviewstudio.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reviewstudio.model.AvatarProfile
import com.example.reviewstudio.model.ProductInfo
import com.example.reviewstudio.model.RenderJob
import com.example.reviewstudio.ui.theme.AccentOrange
import com.example.reviewstudio.ui.theme.BorderColor
import com.example.reviewstudio.ui.theme.DarkSurface
import com.example.reviewstudio.ui.theme.SuccessGreen
import com.example.reviewstudio.ui.theme.TextPrimary
import com.example.reviewstudio.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun RenderingStepView(
    job: RenderJob,
    product: ProductInfo,
    avatar: AvatarProfile
) {
    var elapsedSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(job.status) {
        while (job.status == "processing") {
            delay(1000)
            elapsedSeconds += 1
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = job.progress / 100f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "render_progress"
    )

    val stages = listOf(
        "สังเคราะห์เสียงบรรยายภาษาไทย" to (job.progress >= 25),
        "ซิงค์ริมฝีปากและท่าทาง (AI Lip-Sync)" to (job.progress >= 55),
        "วางภาพสินค้า ป้ายราคา และองค์ประกอบแนวตั้ง" to (job.progress >= 80),
        "เรนเดอร์และส่งออกไฟล์วิดีโอ 1080p" to (job.progress >= 100)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                .testTag("rendering_card")
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF431407)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        color = AccentOrange,
                        trackColor = Color(0xFF1E293B),
                        strokeWidth = 5.dp,
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = "${job.progress}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "กำลังเรนเดอร์วิดีโอรีวิวสินค้า...",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = job.stage.ifBlank { "กำลังประมวลผลโมเดล AI Avatar..." },
                        fontSize = 12.sp,
                        color = AccentOrange
                    )
                }

                // Elapsed time info
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0F172A))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassTop,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "เวลาที่ใช้ไป: ${elapsedSeconds} วินาที (ประมาณการ 1-3 นาที)",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Divider(color = BorderColor, thickness = 1.dp)

                // Stage Checklist
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    stages.forEach { (stageText, isDone) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(if (isDone) SuccessGreen else Color(0xFF1E293B)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF64748B))
                                    )
                                }
                            }

                            Text(
                                text = stageText,
                                fontSize = 12.sp,
                                color = if (isDone) TextPrimary else TextSecondary,
                                fontWeight = if (isDone) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}
