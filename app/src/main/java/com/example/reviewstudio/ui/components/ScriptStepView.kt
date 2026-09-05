package com.example.reviewstudio.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reviewstudio.model.ProductInfo
import com.example.reviewstudio.model.ScriptTone
import com.example.reviewstudio.ui.theme.AccentOrange
import com.example.reviewstudio.ui.theme.BorderColor
import com.example.reviewstudio.ui.theme.DarkSurface
import com.example.reviewstudio.ui.theme.TextPrimary
import com.example.reviewstudio.ui.theme.TextSecondary

@Composable
fun ScriptStepView(
    product: ProductInfo,
    script: String,
    selectedTone: ScriptTone,
    selectedDuration: Int,
    isGenerating: Boolean,
    onScriptChange: (String) -> Unit,
    onToneChange: (ScriptTone) -> Unit,
    onDurationChange: (Int) -> Unit,
    onRegenerate: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        if (script.isBlank()) {
            onRegenerate()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Box 1: Controls
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
                    text = "ปรับแต่งสไตล์สคริปต์รีวิว",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                // Tone selection
                Text("โทนเสียงและสไตล์การพูด:", fontSize = 12.sp, color = TextSecondary)
                ScriptTone.values().forEach { tone ->
                    val isSelected = tone == selectedTone
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF1E293B) else Color(0xFF0F172A))
                            .border(
                                1.dp,
                                if (isSelected) AccentOrange else BorderColor,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { onToneChange(tone) }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onToneChange(tone) },
                            colors = RadioButtonDefaults.colors(selectedColor = AccentOrange)
                        )
                        Column(modifier = Modifier.padding(start = 6.dp)) {
                            Text(
                                text = tone.titleTh,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) AccentOrange else TextPrimary
                            )
                            Text(
                                text = tone.descTh,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                // Duration selection
                Text("ความยาววิดีโอเป้าหมาย:", fontSize = 12.sp, color = TextSecondary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(15 to "15 วินาที", 30 to "30 วินาที", 60 to "60 วินาที").forEach { (sec, label) ->
                        val isSelected = sec == selectedDuration
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFF1E293B) else Color(0xFF0F172A))
                                .border(
                                    1.dp,
                                    if (isSelected) AccentOrange else BorderColor,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { onDurationChange(sec) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) AccentOrange else TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Box 2: Script Editor
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "สคริปต์รีวิวภาษาไทย",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        IconButton(
                            onClick = onRegenerate,
                            enabled = !isGenerating,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Regenerate",
                                tint = AccentOrange,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Review Script", script)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "คัดลอกสคริปต์แล้ว", Toast.LENGTH_SHORT).show()
                            },
                            enabled = script.isNotBlank(),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                if (isGenerating) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF431407))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            color = AccentOrange,
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "AI กำลังวิเคราะห์สินค้าและเขียนสคริปต์...",
                            fontSize = 11.sp,
                            color = Color(0xFFFFEDD5)
                        )
                    }
                }

                OutlinedTextField(
                    value = script,
                    onValueChange = onScriptChange,
                    minLines = 8,
                    maxLines = 12,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentOrange,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("script_input_field")
                )

                Text(
                    text = "* คุณสามารถแก้ไขข้อความสคริปต์ได้อิสระตามต้องการ",
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                // Navigation Buttons
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
                            .height(46.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ย้อนกลับ", fontSize = 13.sp)
                    }

                    Button(
                        onClick = onNext,
                        enabled = script.isNotBlank() && !isGenerating,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(46.dp)
                            .testTag("next_to_avatar_button")
                    ) {
                        Text("ต่อไป: เลือก AI Avatar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
