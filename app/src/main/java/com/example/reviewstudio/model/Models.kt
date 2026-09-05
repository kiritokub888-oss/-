package com.example.reviewstudio.model

import androidx.annotation.DrawableRes

data class Product(
    val id: String,
    val nameTh: String,
    val nameEn: String,
    val category: String,
    val priceThb: Int,
    val originalPriceThb: Int? = null,
    val sellingPoints: List<String>,
    val targetAudience: String,
    @DrawableRes val imageResId: Int? = null,
    val customImageUrl: String? = null
)

data class StoryboardScene(
    val sceneNumber: Int,
    val titleTh: String,
    val timeDurationSec: Int,
    val visualBrollPrompt: String,
    val spokenNarrationTh: String,
    val onScreenSubtitleTh: String,
    val highlightKeyword: String
)

data class ReviewScript(
    val id: String,
    val productName: String,
    val platform: PlatformType,
    val tone: ToneOfVoice,
    val hook3SecondsTh: String,
    val hookAlternativeTh: String,
    val totalEstimatedDurationSec: Int,
    val scenes: List<StoryboardScene>,
    val fullVoiceoverScriptTh: String,
    val captionTextTh: String,
    val hashtags: List<String>,
    val callToActionTh: String,
    val createdAtMillis: Long = System.currentTimeMillis()
)
