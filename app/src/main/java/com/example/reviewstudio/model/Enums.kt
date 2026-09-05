package com.example.reviewstudio.model

enum class PlatformType(
    val title: String,
    val thaiLabel: String,
    val iconEmoji: String,
    val primaryColor: Long,
    val defaultAspect: String
) {
    TIKTOK(
        title = "TikTok Shop",
        thaiLabel = "ติ๊กต๊อกช็อป (ตะกร้าเหลือง)",
        iconEmoji = "🎵",
        primaryColor = 0xFFFE2C55,
        defaultAspect = "9:16"
    ),
    SHOPEE(
        title = "Shopee Video",
        thaiLabel = "ช้อปปี้วิดีโอ (ป้ายยาถูกชัวร์)",
        iconEmoji = "🛍️",
        primaryColor = 0xFFEE4D2D,
        defaultAspect = "9:16"
    ),
    REELS(
        title = "IG / FB Reels",
        thaiLabel = "ไอจี / เฟซบุ๊กรีลส์",
        iconEmoji = "📸",
        primaryColor = 0xFFE1306C,
        defaultAspect = "9:16"
    )
}

enum class ToneOfVoice(
    val title: String,
    val thaiDescription: String,
    val tag: String
) {
    PEER_REVIEW(
        title = "เพื่อนบอกต่อ (Peer-to-Peer)",
        thaiDescription = "รีวิวจริงใจ เป็นกันเอง ฟีลเพื่อนป้ายยาเพื่อน",
        tag = "#ของดีบอกต่อ"
    ),
    FLASH_SALE(
        title = "โปรไฟไหม้ (Flash Sale Urgency)",
        thaiDescription = "เร่งด่วน กระตุ้นยอดขาย โค้ดลับ ลดจำกัดเวลา",
        tag = "#รีบกดก่อนหมด"
    ),
    EXPERT_AESTHETIC(
        title = "ผู้เชี่ยวชาญ/มินิมอล (Aesthetic & Expert)",
        thaiDescription = "เน้นสาระ ความน่าเชื่อถือ อธิบายกลไกสัมผัส",
        tag = "#สาระน่ารู้"
    ),
    ENTERTAINING(
        title = "สายฮาเปิดตัวปัง (Fun & Dramatic)",
        thaiDescription = "เปิดด้วยคำถามชวนอึ้ง สนุก เล่าเรื่องน่าติดตาม",
        tag = "#เตือนแล้วนะ"
    )
}
