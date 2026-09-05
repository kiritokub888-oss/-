package com.example.reviewstudio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.reviewstudio.model.AvatarProfile
import com.example.reviewstudio.model.ProductInfo
import com.example.reviewstudio.ui.theme.AccentOrange
import com.example.reviewstudio.ui.theme.BorderColor
import com.example.reviewstudio.ui.theme.DarkSurface
import com.example.reviewstudio.ui.theme.TextPrimary
import com.example.reviewstudio.ui.theme.TextSecondary

@Composable
fun AvatarStepView(
    avatars: List<AvatarProfile>,
    selectedAvatar: AvatarProfile,
    aspectRatio: String,
    product: ProductInfo,
    onAvatarSelect: (AvatarProfile) -> Unit,
    onAspectRatioChange: (String) -> Unit,
    onBack: () -> Unit,
    onStartRender: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Box 1: Avatar Selection
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "เลือก AI Avatar ผู้บรรยายรีวิวสินค้า",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "ครีเอเตอร์ AI พูดภาษาไทยชัดเจน เป็นธรรมชาติ",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                avatars.forEach { avatar ->
                    val isSelected = avatar.id == selectedAvatar.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color(0xFF1E293B) else Color(0xFF0F172A))
                            .border(
                                1.dp,
                                if (isSelected) AccentOrange else BorderColor,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { onAvatarSelect(avatar) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF334155)),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = avatar.imageUrl,
                                contentDescription = avatar.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = avatar.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) AccentOrange else TextPrimary
                                )
                            }
                            Text(
                                text = avatar.category,
                                fontSize = 11.sp,
                                color = AccentOrange
                            )
                            Text(
                                text = avatar.description,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                maxLines = 2
                            )
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(AccentOrange),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Box 2: Ratio & Format
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "ขนาดวิดีโอ (Aspect Ratio)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 9:16
                    val is916 = aspectRatio == "9:16"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (is916) Color(0xFF1E293B) else Color(0xFF0F172A))
                            .border(1.dp, if (is916) AccentOrange else BorderColor, RoundedCornerShape(8.dp))
                            .clickable { onAspectRatioChange("9:16") }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Smartphone, contentDescription = null, tint = if (is916) AccentOrange else TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("9:16 แนวตั้ง", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (is916) AccentOrange else TextPrimary)
                            Text("TikTok / Reels", fontSize = 10.sp, color = TextSecondary)
                        }
                    }

                    // 16:9
                    val is169 = aspectRatio == "16:9"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (is169) Color(0xFF1E293B) else Color(0xFF0F172A))
                            .border(1.dp, if (is169) AccentOrange else BorderColor, RoundedCornerShape(8.dp))
                            .clickable { onAspectRatioChange("16:9") }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Tv, contentDescription = null, tint = if (is169) AccentOrange else TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("16:9 แนวนอน", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (is169) AccentOrange else TextPrimary)
                            Text("YouTube / Web", fontSize = 10.sp, color = TextSecondary)
                        }
                    }
                }

                // Actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ย้อนกลับ", fontSize = 13.sp)
                    }

                    Button(
                        onClick = onStartRender,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                            .testTag("start_render_button")
                    ) {
                        Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("สร้างและเรนเดอร์วิดีโอ", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
