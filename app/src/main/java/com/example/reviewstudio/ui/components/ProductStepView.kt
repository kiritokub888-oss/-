package com.example.reviewstudio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.reviewstudio.model.ProductInfo
import com.example.reviewstudio.ui.theme.AccentOrange
import com.example.reviewstudio.ui.theme.BorderColor
import com.example.reviewstudio.ui.theme.DarkSurface
import com.example.reviewstudio.ui.theme.SuccessGreen
import com.example.reviewstudio.ui.theme.TextPrimary
import com.example.reviewstudio.ui.theme.TextSecondary
import com.example.reviewstudio.ui.theme.WarningAmber

@Composable
fun ProductStepView(
    product: ProductInfo,
    isExtracting: Boolean,
    extractError: String?,
    extractSuccess: Boolean,
    onProductChange: (ProductInfo) -> Unit,
    onExtractUrl: (String) -> Unit,
    onSelectSample: (Int) -> Unit,
    onNext: () -> Unit
) {
    var urlInput by remember { mutableStateOf(product.url) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Box 1: URL input card
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                .testTag("url_card")
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = "Link Icon",
                        tint = AccentOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "วางลิงก์สินค้า (Shopee / Lazada)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Text(
                    text = "ระบบจะพยายามดึงข้อมูลอัตโนมัติ หรือกรอกข้อมูลเองด้านล่างได้ทันที",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                OutlinedTextField(
                    value = urlInput,
                    onValueChange = { urlInput = it },
                    placeholder = { Text("https://shopee.co.th/product/...", color = Color.Gray, fontSize = 13.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentOrange,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = AccentOrange
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("url_input_field")
                )

                Button(
                    onClick = { onExtractUrl(urlInput) },
                    enabled = !isExtracting && urlInput.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("extract_button")
                ) {
                    if (isExtracting) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("กำลังดึงข้อมูลสินค้า...", fontSize = 13.sp)
                    } else {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ดึงข้อมูลสินค้าอัตโนมัติ", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Sample buttons
                Text(
                    text = "หรือทดลองด้วยตัวอย่างสินค้า:",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("หูฟัง ANC", "เซรั่มหน้าใส", "พัดลมเทอร์โบ").forEachIndexed { idx, label ->
                        OutlinedButton(
                            onClick = { onSelectSample(idx) },
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = label, fontSize = 10.sp, maxLines = 1)
                        }
                    }
                }

                // Success or Error Feedback
                if (extractSuccess) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF064E3B))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                        Text("ดึงข้อมูลสินค้าสำเร็จ! สามารถปรับแต่งรายละเอียดด้านล่างได้", fontSize = 12.sp, color = Color(0xFFA7F3D0))
                    }
                }

                if (extractError != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF451A03))
                            .padding(10.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(16.dp))
                        Text(extractError, fontSize = 11.sp, color = Color(0xFFFDE68A))
                    }
                }
            }
        }

        // Box 2: Manual product form (Guarantees zero freeze, always functional)
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                .testTag("product_form_card")
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Form",
                        tint = AccentOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "รายละเอียดสินค้าสำหรับทำรีวิว",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                // Title
                Text("ชื่อสินค้า *", fontSize = 12.sp, color = TextSecondary)
                OutlinedTextField(
                    value = product.title,
                    onValueChange = { onProductChange(product.copy(title = it)) },
                    placeholder = { Text("เช่น หูฟังบลูทูธไร้สาย ANC ตัดเสียงรบกวน", color = Color.Gray, fontSize = 13.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentOrange,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("product_title_input")
                )

                // Prices
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("ราคาโปรโมชัน (บาท) *", fontSize = 12.sp, color = TextSecondary)
                        OutlinedTextField(
                            value = product.price,
                            onValueChange = { onProductChange(product.copy(price = it)) },
                            placeholder = { Text("เช่น 490", color = Color.Gray, fontSize = 13.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentOrange,
                                unfocusedBorderColor = BorderColor,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("product_price_input")
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("ราคาเดิม (บาท)", fontSize = 12.sp, color = TextSecondary)
                        OutlinedTextField(
                            value = product.originalPrice,
                            onValueChange = { onProductChange(product.copy(originalPrice = it)) },
                            placeholder = { Text("เช่น 1,290", color = Color.Gray, fontSize = 13.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentOrange,
                                unfocusedBorderColor = BorderColor,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Description
                Text("จุดขาย / รายละเอียดสินค้า", fontSize = 12.sp, color = TextSecondary)
                OutlinedTextField(
                    value = product.description,
                    onValueChange = { onProductChange(product.copy(description = it)) },
                    placeholder = { Text("ระบุคุณสมบัติเด่น ประโยชน์ หรือวิธีใช้งาน...", color = Color.Gray, fontSize = 13.sp) },
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentOrange,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Image Preview
                if (product.imageUrl.isNotBlank()) {
                    Text("รูปภาพสินค้า", fontSize = 12.sp, color = TextSecondary)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F172A)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = "Product Image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // Next Button
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onNext,
                    enabled = product.title.isNotBlank() && product.price.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("next_to_script_button")
                ) {
                    Text("ต่อไป: ให้ AI เขียนสคริปต์รีวิว", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
