package com.example.reviewstudio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Videocam
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
import com.example.reviewstudio.ui.theme.AccentOrange
import com.example.reviewstudio.ui.theme.BorderColor
import com.example.reviewstudio.ui.theme.DarkSurface
import com.example.reviewstudio.ui.theme.TextPrimary
import com.example.reviewstudio.ui.theme.TextSecondary

@Composable
fun HeaderBar(
    onOpenApiInfo: () -> Unit
) {
    Surface(
        color = DarkSurface,
        border = null,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("header_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AccentOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "App Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = "สร้างวิดีโอรีวิวสินค้าด้วย AI",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Shopee / Lazada → สคริปต์ → AI Avatar ภาษาไทย",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            IconButton(
                onClick = onOpenApiInfo,
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1E293B))
                    .testTag("api_key_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Key,
                    contentDescription = "API Keys",
                    tint = AccentOrange,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
