package com.example.reviewstudio.data

import com.example.reviewstudio.BuildConfig
import com.example.reviewstudio.model.PlatformType
import com.example.reviewstudio.model.Product
import com.example.reviewstudio.model.ReviewScript
import com.example.reviewstudio.model.StoryboardScene
import com.example.reviewstudio.model.ToneOfVoice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

object ReviewGeneratorEngine {

    suspend fun generateReviewScript(
        product: Product,
        platform: PlatformType,
        tone: ToneOfVoice,
        customPromptNotes: String = ""
    ): ReviewScript = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        if (!apiKey.isNullOrBlank() && apiKey != "GEMINI_API_KEY") {
            try {
                val geminiScript = callGeminiApi(apiKey, product, platform, tone, customPromptNotes)
                if (geminiScript != null) {
                    return@withContext geminiScript
                }
            } catch (_: Exception) {
                // Fall back gracefully to built-in algorithmic engine
            }
        }

        // Built-in intelligent Thai viral review engine
        generateAlgorithmicScript(product, platform, tone)
    }

    private fun callGeminiApi(
        apiKey: String,
        product: Product,
        platform: PlatformType,
        tone: ToneOfVoice,
        notes: String
    ): ReviewScript? {
        val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
        val url = URL(endpoint)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json; utf-8")
        conn.doOutput = true
        conn.connectTimeout = 30000
        conn.readTimeout = 30000

        val systemPrompt = """
            You are an expert Thai e-commerce viral short-video copywriter for TikTok Shop, Shopee Video, and IG Reels.
            Write a high-converting, natural Thai language video review script for the given product.
            Output ONLY valid JSON matching this schema:
            {
              "hook3Seconds": "คำเปิดคลิป 3 วินาทีแรกหยุดนิ้ว",
              "hookAlternative": "คำเปิดทางเลือกอีกแบบ",
              "scenes": [
                {
                  "sceneNumber": 1,
                  "title": "ชื่อฉาก",
                  "durationSec": 5,
                  "visualBroll": "คำแนะนำมุมกล้องและการถ่ายทำ",
                  "narration": "คำพากย์ภาษาไทยตรงนี้",
                  "subtitle": "ข้อความซับบนจอ",
                  "highlightKeyword": "คำไฮไลท์สีเด่น"
                }
              ],
              "caption": "แคปชั่นโพสต์วิดีโอ",
              "hashtags": ["#แฮชแท็ก1", "#แฮชแท็ก2"],
              "callToAction": "ประโยคปิดท้ายชวนกดซื้อ"
            }
        """.trimIndent()

        val userPrompt = """
            สร้างสคริปต์วิดีโอรีวิวแนวตั้ง 9:16 ความยาว 30-45 วินาที
            สินค้า: ${product.nameTh} (${product.category})
            ราคา: ${product.priceThb} บาท (จากปกติ ${product.originalPriceThb ?: (product.priceThb * 2)} บาท)
            จุดขายหลัก: ${product.sellingPoints.joinToString(", ")}
            กลุ่มเป้าหมาย: ${product.targetAudience}
            แพลตฟอร์ม: ${platform.title} (${platform.thaiLabel})
            โทนเสียง: ${tone.title} (${tone.thaiDescription})
            ${if (notes.isNotBlank()) "คำขอเพิ่มเติม: $notes" else ""}
            
            ให้เขียน 5 ฉาก (Scene 1: Hook/ปัญหา, Scene 2: เปิดตัวสินค้า, Scene 3: สาธิต/ความรู้สึก, Scene 4: ความคุ้มค่า/รีวิว, Scene 5: ปิดการขาย CTA)
            ภาษาไทยต้องลื่นไหล ธรรมชาติ เป็นสำนวนคนไทยจริง ไม่แปลตรงตัว
        """.trimIndent()

        val rootJson = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", "$systemPrompt\n\n$userPrompt"))
                    })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("responseMimeType", "application/json")
                put("temperature", 0.7)
            })
        }

        OutputStreamWriter(conn.outputStream).use { writer ->
            writer.write(rootJson.toString())
            writer.flush()
        }

        if (conn.responseCode == 200) {
            val responseText = BufferedReader(InputStreamReader(conn.inputStream)).use { it.readText() }
            val responseJson = JSONObject(responseText)
            val candidates = responseJson.optJSONArray("candidates")
            val contentObj = candidates?.optJSONObject(0)?.optJSONObject("content")
            val parts = contentObj?.optJSONArray("parts")
            val rawText = parts?.optJSONObject(0)?.optString("text") ?: return null

            val parsedJson = JSONObject(rawText)
            val scenesArray = parsedJson.optJSONArray("scenes") ?: JSONArray()
            val scenesList = mutableListOf<StoryboardScene>()
            val fullVoiceoverBuilder = StringBuilder()

            for (i in 0 until scenesArray.length()) {
                val sc = scenesArray.getJSONObject(i)
                val scene = StoryboardScene(
                    sceneNumber = sc.optInt("sceneNumber", i + 1),
                    titleTh = sc.optString("title", "ฉากที่ ${i + 1}"),
                    timeDurationSec = sc.optInt("durationSec", 6),
                    visualBrollPrompt = sc.optString("visualBroll", "โชว์สินค้ามุมสวย"),
                    spokenNarrationTh = sc.optString("narration", ""),
                    onScreenSubtitleTh = sc.optString("subtitle", ""),
                    highlightKeyword = sc.optString("highlightKeyword", "")
                )
                scenesList.add(scene)
                fullVoiceoverBuilder.append(scene.spokenNarrationTh).append(" ")
            }

            val tagsList = mutableListOf<String>()
            val hashtagsJson = parsedJson.optJSONArray("hashtags")
            if (hashtagsJson != null) {
                for (j in 0 until hashtagsJson.length()) {
                    tagsList.add(hashtagsJson.getString(j))
                }
            }
            if (tagsList.isEmpty()) {
                tagsList.addAll(listOf("#รีวิวของดี", "#ของมันต้องมี", "#ป้ายยา", "#พิกัดในตะกร้า"))
            }

            return ReviewScript(
                id = UUID.randomUUID().toString(),
                productName = product.nameTh,
                platform = platform,
                tone = tone,
                hook3SecondsTh = parsedJson.optString("hook3Seconds", "หยุดดูก่อนถ้าไม่อยากพลาดของดี!"),
                hookAlternativeTh = parsedJson.optString("hookAlternative", "บอกเลยว่าตัวนี้เปลี่ยนชีวิตมาก"),
                totalEstimatedDurationSec = scenesList.sumOf { it.timeDurationSec },
                scenes = scenesList,
                fullVoiceoverScriptTh = fullVoiceoverBuilder.toString().trim(),
                captionTextTh = parsedJson.optString("caption", "${product.nameTh} ลดพิเศษวันนี้ กดในตะกร้าได้เลย!"),
                hashtags = tagsList,
                callToActionTh = parsedJson.optString("callToAction", "รีบจิ้มตะกร้าเหลืองซ้ายมือตอนนี้เลย!")
            )
        }
        return null
    }

    private fun generateAlgorithmicScript(
        product: Product,
        platform: PlatformType,
        tone: ToneOfVoice
    ): ReviewScript {
        val points = product.sellingPoints
        val p1 = points.getOrNull(0) ?: "ใช้ดีมากจนต้องบอกต่อ"
        val p2 = points.getOrNull(1) ?: "คุณภาพเกินราคาไปไกล"
        val p3 = points.getOrNull(2) ?: "คุ้มค่า ประหยัดเวลา"
        val p4 = points.getOrNull(3) ?: "การันตียอดขายและรีวิวเพียบ"

        val (hook, hookAlt) = when (tone) {
            ToneOfVoice.PEER_REVIEW -> Pair(
                "แกรรร! ใครที่มีปัญหาเรื่องนี้เหมือนฉัน หยุดดูก่อน วันนี้เจอตัวจบแล้วจริง!",
                "ถ้ามีเพื่อนมาถามว่าชิ้นไหนควรตำที่สุดในเดือนนี้ ฉันชี้ตัวนี้ตัวเดียวเลย!"
            )
            ToneOfVoice.FLASH_SALE -> Pair(
                "เตือนแล้วนะ! ใครเล็งตัวนี้อยู่ กดด่วนก่อนของจะหมด เพราะเค้าจัดโปรลดโหดมาก!",
                "อย่าเพิ่งเลื่อนผ่าน! ถ้ายังไม่ได้กดเก็บคูปองลดพิเศษรอบนี้ พลาดแล้วจะเสียดาย!"
            )
            ToneOfVoice.EXPERT_AESTHETIC -> Pair(
                "ทำไมไอเทมนี้ถึงเป็นกระแสไวรัลขนาดนี้? วันนี้มาเจาะลึกให้ฟังแบบหมดเปลือกค่ะ",
                "หลายคนสงสัยว่ามันดีจริงไหม คุ้มราคาหรือเปล่า คลิปนี้มีคำตอบชัดเจน"
            )
            ToneOfVoice.ENTERTAINING -> Pair(
                "ตอนแรกคิดว่าหลอกขาย! แต่พอลองใช้จริง... ถึงกับต้องกราบขอโทษคนขายเลยอ่ะ!",
                "อย่าหาซื้อตามถ้ายังไม่พร้อมติดใจ! ตัวนี้ทำชีวิตวุ่นวายเพราะขาดไม่ได้แล้ว!"
            )
        }

        val cta = when (platform) {
            PlatformType.TIKTOK -> "ใครอยากได้ พิกัดจิ้มที่ตะกร้าเหลืองซ้ายมือได้เลยนะ รีบกดก่อนหมดโปร!"
            PlatformType.SHOPEE -> "พิกัดในปุ่มวิดีโอสีส้มเลยจ้า เก็บโค้ดลดเพิ่มส่งฟรี คุ้มมาก!"
            PlatformType.REELS -> "พิกัดและส่วนลดพิเศษ ปักหมุดไว้ที่ลิงก์หน้าโปรไฟล์แล้วน้า กดดูได้เลย!"
        }

        val scenes = listOf(
            StoryboardScene(
                sceneNumber = 1,
                titleTh = "Hook & ปัญหาที่เจอ (0-5s)",
                timeDurationSec = 5,
                visualBrollPrompt = "ถือสินค้าคู่กับใบหน้า โคลสอัพสีหน้าตกใจหรือสงสัย ดึงดูดสายตาทันที",
                spokenNarrationTh = "$hook เชื่อว่าทุกคนเจอปัญหานี้กันเยอะมาก",
                onScreenSubtitleTh = hook,
                highlightKeyword = "เจอตัวจบแล้ว!"
            ),
            StoryboardScene(
                sceneNumber = 2,
                titleTh = "เปิดตัวสินค้า & ดีไซน์ (5-12s)",
                timeDurationSec = 7,
                visualBrollPrompt = "ภาพหมุน 360 องศา โชว์แพ็กเกจจิ้ง สัมผัสวัสดุ และขนาดที่แท้จริงแบบมินิมอล",
                spokenNarrationTh = "นี่เลยจ้า ${product.nameTh} ดูความพรีเมียมของเค้า $p1 ดีงามแบบตะโกน",
                onScreenSubtitleTh = "${product.nameTh}\n$p1",
                highlightKeyword = "ดูความพรีเมียม"
            ),
            StoryboardScene(
                sceneNumber = 3,
                titleTh = "ทดสอบการใช้งานจริง (12-22s)",
                timeDurationSec = 10,
                visualBrollPrompt = "ภาพตัดต่อเร็ว (Fast cuts) สาธิตวิธีใช้ เสียง ASMR และผลลัพธ์ทันที",
                spokenNarrationTh = "มาลองใช้ให้ดูชัดๆ เลย จะเห็นเลยว่า $p2 และที่ชอบมากๆ คือ $p3 ใช้งานง่ายจริง",
                onScreenSubtitleTh = "ทดสอบจริง: $p2",
                highlightKeyword = "ผลลัพธ์เห็นชัด"
            ),
            StoryboardScene(
                sceneNumber = 4,
                titleTh = "ความคุ้มค่า & รีวิวการันตี (22-30s)",
                timeDurationSec = 8,
                visualBrollPrompt = "วางเทียบขนาด หรือโชว์ฟังก์ชันเสริม พร้อมภาพแคปเจอร์คะแนนรีวิว 5 ดาว",
                spokenNarrationTh = "จากราคาปกติ ${product.originalPriceThb ?: (product.priceThb + 200)} บาท ตอนนี้จัดเหลือแค่ ${product.priceThb} บาทเท่านั้น $p4 คุ้มเกินเบอร์มาก",
                onScreenSubtitleTh = "พิเศษเพียง ${product.priceThb}.- เท่านั้น (จำนวนจำกัด)",
                highlightKeyword = "ลดเหลือ ${product.priceThb}.-"
            ),
            StoryboardScene(
                sceneNumber = 5,
                titleTh = "Call to Action ปิดการขาย (30-36s)",
                timeDurationSec = 6,
                visualBrollPrompt = "ชี้นิ้วลงไปที่มุมซ้ายล่าง หรือโบกมือชวนกดสั่งซื้อ แสดงไอคอนรถเข็นหรือตะกร้า",
                spokenNarrationTh = cta,
                onScreenSubtitleTh = cta,
                highlightKeyword = "รีบกดเลยก่อนหมด!"
            )
        )

        val fullVoiceover = scenes.joinToString(" ") { it.spokenNarrationTh }

        val hashtags = mutableListOf(
            "#${product.category.substringBefore(" ").replace("&", "")}",
            "#รีวิวของดี",
            "#ของมันต้องมี",
            "#ป้ายยาของใช้",
            when (platform) {
                PlatformType.TIKTOK -> "#TikTokป้ายยา #พิกัดในตะกร้า"
                PlatformType.SHOPEE -> "#Shopeeป้ายยา #ช้อปปี้ถูกชัวร์"
                PlatformType.REELS -> "#ReelsTH #ของดีบอกต่อ"
            },
            tone.tag
        ).flatMap { it.split(" ") }.filter { it.startsWith("#") }.distinct()

        val caption = """
            ป้ายยาตัวเด็ด! ${product.nameTh}
            ✨ $p1
            🔥 พิเศษเหลือเพียง ${product.priceThb}.- เท่านั้น
            
            $cta
        """.trimIndent()

        return ReviewScript(
            id = UUID.randomUUID().toString(),
            productName = product.nameTh,
            platform = platform,
            tone = tone,
            hook3SecondsTh = hook,
            hookAlternativeTh = hookAlt,
            totalEstimatedDurationSec = scenes.sumOf { it.timeDurationSec },
            scenes = scenes,
            fullVoiceoverScriptTh = fullVoiceover,
            captionTextTh = caption,
            hashtags = hashtags,
            callToActionTh = cta
        )
    }
}
