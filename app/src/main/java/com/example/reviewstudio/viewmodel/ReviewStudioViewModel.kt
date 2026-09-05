package com.example.reviewstudio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reviewstudio.model.AvatarProfile
import com.example.reviewstudio.model.ProductInfo
import com.example.reviewstudio.model.RenderJob
import com.example.reviewstudio.model.ReviewStudioStep
import com.example.reviewstudio.model.ScriptTone
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewStudioViewModel : ViewModel() {

    val avatarList = listOf(
        AvatarProfile(
            id = "avatar-meena",
            name = "มีนา (Meena)",
            gender = "female",
            category = "บิวตี้ / สกินแคร์ / ไลฟ์สไตล์",
            description = "น้ำเสียงสดใส เป็นธรรมชาติ เหมาะกับการรีวิวสินค้าความงามและของใช้ส่วนตัว",
            voiceName = "ภาษาไทย - นุ่มนวลสดใส (Standard Female A)",
            imageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600&auto=format&fit=crop&q=80"
        ),
        AvatarProfile(
            id = "avatar-boy",
            name = "บอย (Boy)",
            gender = "male",
            category = "ไอที / แกดเจ็ต / ของแต่งโต๊ะ",
            description = "สไตล์สบายๆ รีวิวชัดเจน อธิบายสเปกเข้าใจง่าย เหมาะกับสินค้าเทคโนโลยี",
            voiceName = "ภาษาไทย - ชัดเจนมั่นใจ (Standard Male A)",
            imageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600&auto=format&fit=crop&q=80"
        ),
        AvatarProfile(
            id = "avatar-fahsai",
            name = "ฟ้าใส (Fahsai)",
            gender = "female",
            category = "ป้ายยาไวรัล / แม่ค้าไลฟ์สด TikTok",
            description = "พลังบวกสูง ดึงดูดความสนใจได้ดี เหมาะกับการป้ายยาสินค้าลดราคาและโปรโมชัน",
            voiceName = "ภาษาไทย - ตื่นเต้นมีพลัง (Expressive Female B)",
            imageUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=600&auto=format&fit=crop&q=80"
        ),
        AvatarProfile(
            id = "avatar-nont",
            name = "นนท์ (Nont)",
            gender = "male",
            category = "มืออาชีพ / สินค้าพรีเมียม / สุขภาพ",
            description = "โทนเสียงอบอุ่น น่าเชื่อถือ เหมาะกับการรีวิวสินค้าสุขภาพ เครื่องใช้ไฟฟ้า",
            voiceName = "ภาษาไทย - นุ่มลึกน่าเชื่อถือ (Warm Male B)",
            imageUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=600&auto=format&fit=crop&q=80"
        )
    )

    private val _currentStep = MutableStateFlow(ReviewStudioStep.PRODUCT)
    val currentStep = _currentStep.asStateFlow()

    private val _product = MutableStateFlow(ProductInfo())
    val product = _product.asStateFlow()

    private val _isExtracting = MutableStateFlow(false)
    val isExtracting = _isExtracting.asStateFlow()

    private val _extractError = MutableStateFlow<String?>(null)
    val extractError = _extractError.asStateFlow()

    private val _extractSuccess = MutableStateFlow(false)
    val extractSuccess = _extractSuccess.asStateFlow()

    private val _script = MutableStateFlow("")
    val script = _script.asStateFlow()

    private val _selectedTone = MutableStateFlow(ScriptTone.EXCITED)
    val selectedTone = _selectedTone.asStateFlow()

    private val _selectedDuration = MutableStateFlow(30)
    val selectedDuration = _selectedDuration.asStateFlow()

    private val _targetPlatform = MutableStateFlow("TikTok")
    val targetPlatform = _targetPlatform.asStateFlow()

    private val _isGeneratingScript = MutableStateFlow(false)
    val isGeneratingScript = _isGeneratingScript.asStateFlow()

    private val _selectedAvatar = MutableStateFlow(avatarList[0])
    val selectedAvatar = _selectedAvatar.asStateFlow()

    private val _aspectRatio = MutableStateFlow("9:16")
    val aspectRatio = _aspectRatio.asStateFlow()

    private val _renderJob = MutableStateFlow(RenderJob())
    val renderJob = _renderJob.asStateFlow()

    fun updateProduct(newProduct: ProductInfo) {
        _product.value = newProduct
    }

    fun updateScript(newScript: String) {
        _script.value = newScript
    }

    fun setTone(tone: ScriptTone) {
        _selectedTone.value = tone
        generateScript()
    }

    fun setDuration(duration: Int) {
        _selectedDuration.value = duration
        generateScript()
    }

    fun setTargetPlatform(platform: String) {
        _targetPlatform.value = platform
    }

    fun selectAvatar(avatar: AvatarProfile) {
        _selectedAvatar.value = avatar
    }

    fun setAspectRatio(ratio: String) {
        _aspectRatio.value = ratio
    }

    fun setStep(step: ReviewStudioStep) {
        _currentStep.value = step
    }

    // Step 1: Extract from URL (Shopee / Lazada) with automatic fallback
    fun extractProductFromUrl(url: String) {
        if (url.isBlank()) {
            _extractError.value = "กรุณากรอกลิงก์สินค้าจาก Shopee หรือ Lazada"
            return
        }

        viewModelScope.launch {
            _isExtracting.value = true
            _extractError.value = null
            _extractSuccess.value = false
            delay(1200) // Simulated network processing

            val isShopee = url.contains("shopee", ignoreCase = true)
            val isLazada = url.contains("lazada", ignoreCase = true)
            val isTiktok = url.contains("tiktok", ignoreCase = true)

            if (isShopee || isLazada || isTiktok) {
                _product.value = _product.value.copy(
                    url = url,
                    title = if (isShopee) "หูฟังบลูทูธไร้สาย ANC ตัดเสียงเงียบ แบตอึด 40 ชม." else "เซรั่มวิตามินซีเข้มข้น ลดรอยสิว หน้ากระจ่างใส",
                    price = if (isShopee) "490" else "350",
                    originalPrice = if (isShopee) "1,290" else "790",
                    description = "สินค้ารับประกันศูนย์ไทย 1 ปีเต็ม ยอดขายกว่า 5,000 ชิ้น รีวิว 4.9 ดาว จัดส่งไว 1-2 วัน",
                    highlights = listOf("คุณภาพคุ้มเกินราคา", "จัดส่งรวดเร็วทันใจ", "มีรับประกันของแท้ 100%"),
                    imageUrl = if (isShopee) "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=800&q=80" else "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=800&q=80",
                    platform = if (isShopee) "Shopee" else if (isLazada) "Lazada" else "TikTok Shop"
                )
                _extractSuccess.value = true
            } else {
                // If link cannot be extracted automatically, smoothly prompt manual form without freezing!
                _extractError.value = "ระบบความปลอดภัยของแพลตฟอร์มป้องกันการดึงข้อมูลอัตโนมัติ คุณสามารถกรอกรายละเอียดในฟอร์มด้านล่างได้ทันที"
                _product.value = _product.value.copy(url = url)
            }
            _isExtracting.value = false
        }
    }

    fun loadSampleProduct(index: Int) {
        when (index) {
            0 -> _product.value = ProductInfo(
                url = "https://shopee.co.th/product/sample-anc-earbuds",
                title = "หูฟังบลูทูธไร้สาย ANC ตัดเสียงเงียบสนิท แบตอึด 40 ชม.",
                price = "490",
                originalPrice = "1,290",
                description = "เบสหนักแน่น ตัดเสียงภายนอกได้เงียบกริบ ไมค์ 4 ตัวคุยสายชัดเจน กันน้ำ IPX5",
                highlights = listOf("ตัดเสียงรบกวน ANC เงียบสนิท", "แบตเตอรี่อึดใช้งานได้ถึง 40 ชม.", "ไมค์ 4 ตัวคุยสายคมชัด"),
                imageUrl = "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=800&q=80",
                platform = "Shopee"
            )
            1 -> _product.value = ProductInfo(
                url = "https://www.lazada.co.th/products/sample-vitamin-c-serum",
                title = "เซรั่มวิตามินซีเข้มข้น ลดรอยสิว ผิวโกลว์กระจ่างใส",
                price = "329",
                originalPrice = "690",
                description = "สารสกัดส้มยูซุธรรมชาติ เนื้อบางเบาซึมไว ไม่เหนียวเหนอะหนะ ปราศจากแอลกอฮอล์",
                highlights = listOf("ลดรอยสิวและจุดด่างดำ", "ผิวแลดูกระจ่างใสใน 14 วัน", "อ่อนโยนต่อผิวแพ้ง่าย"),
                imageUrl = "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=800&q=80",
                platform = "Lazada"
            )
            else -> _product.value = ProductInfo(
                url = "https://shop.tiktok.com/sample-turbo-fan",
                title = "พัดลมพกพาพลังเทอร์โบ 5 สปีด จอดิจิทัลคลายร้อน",
                price = "269",
                originalPrice = "450",
                description = "ลมแรงสะใจ น้ำหนักเบา แบตเตอรี่ทนทาน 12 ชั่วโมง พกพาสะดวกไปได้ทุกที่",
                highlights = listOf("ลมแรงปรับได้ 5 ระดับ", "หน้าจอดิจิทัลแสดงแบตเตอรี่", "ชาร์จ Type-C สะดวกรวดเร็ว"),
                imageUrl = "https://images.unsplash.com/photo-1563245372-f21724e3856d?w=800&q=80",
                platform = "TikTok Shop"
            )
        }
        _extractSuccess.value = true
        _extractError.value = null
    }

    // Step 2: Generate Thai AI Script
    fun generateScript() {
        val currentProd = _product.value
        val title = if (currentProd.title.isNotBlank()) currentProd.title else "สินค้าตัวนี้"
        val price = if (currentProd.price.isNotBlank()) currentProd.price else "ราคาพิเศษ"
        val origPrice = if (currentProd.originalPrice.isNotBlank()) currentProd.originalPrice else ""
        val hlText = if (currentProd.highlights.isNotEmpty()) currentProd.highlights.joinToString(" แถมยัง") else "คุณภาพคุ้มราคา ดีไซน์สวยทนทาน"

        viewModelScope.launch {
            _isGeneratingScript.value = true
            delay(800)

            val hook: String
            val bodyContent: String
            val cta: String

            when (_selectedTone.value) {
                ScriptTone.EXCITED -> {
                    hook = "ทุกคนนน! ใครที่กำลังเล็ง \"$title\" อยู่ อย่าเพิ่งเลื่อนผ่านเด็ดขาด!"
                    bodyContent = "ตัวนี้บอกเลยว่ากระแสแรงมาก เพราะได้ทั้ง $hlText ${if (origPrice.isNotBlank()) "จากปกติราคา $origPrice บาท " else ""}แต่วันนี้จัดโปรพิเศษเหลือแค่ $price บาทเท่านั้น! ใช้งานง่าย พกพาสะดวก บอกเลยว่าชีวิตดีขึ้นเยอะ"
                    cta = "ใครไม่อยากพลาดราคาโปรโมชันนี้ รีบกดที่ตะกร้าสีเหลืองมุมซ้ายล่างได้เลย ของหมดไวมาก กดด่วนเลยนะคะ!"
                }
                ScriptTone.CASUAL -> {
                    hook = "มาป้ายยาของดีที่ใช้แล้วชอบมาก วันนี้มาบอกต่อ \"$title\" ครับ"
                    bodyContent = "ส่วนตัวลองใช้มาสักพักแล้ว จุดที่ประทับใจสุดๆ คือ $hlText ที่สำคัญงบสบายกระเป๋ามาก แค่ $price บาท ถือว่าคุ้มค่าเกินราคาไปเยอะ"
                    cta = "พิกัดอยู่ในตะกร้าแล้วนะ ลองไปจิ้มดูกันได้เลย แนะนำจริงๆ ครับ"
                }
                ScriptTone.PROFESSIONAL -> {
                    hook = "เจาะลึกรีวิว \"$title\" ดีจริงไหม คุ้มค่าแก่การลงทุนหรือเปล่า?"
                    bodyContent = "จากการทดสอบจุดเด่นหลักพบว่า $hlText ตอบโจทย์การใช้งานได้จริง ประสิทธิภาพเทียบเคียงแบรนด์ระดับท็อป ในราคาเริ่มต้นเพียง $price บาท"
                    cta = "สำหรับใครที่สนใจตรวจสอบข้อมูลเพิ่มเติมหรือสั่งซื้อของแท้ กดลิงก์ด้านล่างเพื่อรับสิทธิ์ส่วนลดพิเศษได้ทันทีครับ"
                }
                ScriptTone.SHORT -> {
                    hook = "ชิ้นนี้ต้องมีติดบ้าน! สรุปสั้นๆ กับ \"$title\""
                    bodyContent = "เด่นเรื่อง $hlText ราคาเบาๆ แค่ $price บาท"
                    cta = "จิ้มที่ตะกร้าเหลืองสั่งซื้อได้เลย ส่งฟรีถึงบ้านครับ!"
                }
            }

            _script.value = "[ช่วงเปิด / Hook]\n$hook\n\n[เนื้อหารีวิว / Body]\n$bodyContent\n\n[ปิดการขาย / CTA]\n$cta"
            _isGeneratingScript.value = false
        }
    }

    // Step 3 & 4: Start Video Rendering Lifecycle (Showing all states to user)
    fun startRendering() {
        _currentStep.value = ReviewStudioStep.RENDERING
        _renderJob.value = RenderJob(
            id = "job_${System.currentTimeMillis()}",
            status = "processing",
            progress = 10,
            stage = "กำลังเตรียม Avatar และสังเคราะห์เสียงภาษาไทย..."
        )

        viewModelScope.launch {
            delay(1500)
            _renderJob.value = _renderJob.value.copy(
                progress = 35,
                stage = "กำลังซิงค์ริมฝีปาก (Lip-sync) และท่าทาง AI Avatar..."
            )
            delay(2000)
            _renderJob.value = _renderJob.value.copy(
                progress = 70,
                stage = "กำลังผสานภาพสินค้า แถบราคา และกราฟิกแนวตั้ง 1080p..."
            )
            delay(2000)
            _renderJob.value = _renderJob.value.copy(
                progress = 100,
                status = "completed",
                stage = "เรนเดอร์วิดีโอสำเร็จแล้ว พร้อมรับชมและดาวน์โหลด",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
            )
            _currentStep.value = ReviewStudioStep.RESULT
        }
    }

    fun resetAll() {
        _product.value = ProductInfo()
        _script.value = ""
        _selectedAvatar.value = avatarList[0]
        _renderJob.value = RenderJob()
        _extractError.value = null
        _extractSuccess.value = false
        _currentStep.value = ReviewStudioStep.PRODUCT
    }
}
