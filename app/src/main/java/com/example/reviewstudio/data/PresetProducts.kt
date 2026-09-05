package com.example.reviewstudio.data

import com.example.reviewstudio.R
import com.example.reviewstudio.model.Product

object PresetProducts {
    val items: List<Product> = listOf(
        Product(
            id = "serum-glow-01",
            nameTh = "เซรั่มไฮยาหน้าใส Glass Skin Glow 50ml",
            nameEn = "Glass Skin Glow Hyaluronic Serum",
            category = "บิวตี้ & สกินแคร์ (Beauty)",
            priceThb = 290,
            originalPriceThb = 590,
            sellingPoints = listOf(
                "ผิวฉ่ำโกลว์ อิ่มน้ำเหมือนสาวเกาหลีใน 7 วัน",
                "เนื้อบางเบา ซึมไว ไม่เหนียวเหนอะหนะ ไม่อุดตัน",
                "ไฮยาลูรอนเข้มข้น 8 โมเลกุล ล็อคความชุ่มชื้น 72 ชม.",
                "ผ่านการทดสอบ ไม่ระคายเคือง เหมาะกับผิวแพ้ง่าย"
            ),
            targetAudience = "สาวๆ วัยทำงานและนักศึกษาที่นอนดึก ผิวโทรม ผิวแห้งแต่งหน้าไม่ติด",
            imageResId = R.drawable.product_serum
        ),
        Product(
            id = "fan-turbo-02",
            nameTh = "พัดลมพกพาไอเย็น Turbo Cool Mini 100 ระดับ",
            nameEn = "Turbo Cool Mini Handheld Fan",
            category = "แกดเจ็ต & ไอที (Tech)",
            priceThb = 359,
            originalPriceThb = 699,
            sellingPoints = listOf(
                "มอเตอร์เทอร์โบพลังลมแรง ปรับได้ 100 ระดับ",
                "แบตเตอรี่ความจุสูง 5,000 mAh เปิดต่อเนื่องได้ 24 ชั่วโมง",
                "หน้าจอดิจิทัล LED แสดงระดับแรงลมและเปอร์เซ็นต์แบตเตอรี่",
                "ดีไซน์มินิมอล น้ำหนักเบา พกใส่กระเป๋าสะดวกมาก"
            ),
            targetAudience = "คนเดินทาง รถไฟฟ้า เดินตลาด และคนขี้ร้อนในเมืองไทย",
            imageResId = R.drawable.product_fan
        ),
        Product(
            id = "cup-pastel-03",
            nameTh = "แก้วเก็บความเย็น Pastel Duo Lid 900ml",
            nameEn = "Pastel Duo Lid Insulated Tumbler",
            category = "ของใช้ในบ้าน & ไลฟ์สไตล์ (Home/Lifestyle)",
            priceThb = 249,
            originalPriceThb = 450,
            sellingPoints = listOf(
                "สแตนเลส 316 Food Grade เก็บความเย็นได้นาน 24 ชั่วโมง",
                "ฝาปิด 2 ระบบ: มีทั้งช่องหลอดดูดและช่องยกดื่มในตัว",
                "ไม่เป็นหยดน้ำเกาะรอบแก้ว วางในช่องวางแก้วในรถได้พอดี",
                "สีพาสเทลมินิมอล น่ารัก ถือไปไหนคนก็ทัก"
            ),
            targetAudience = "สายคาเฟ่ พนักงานออฟฟิศ และคนที่ชอบดื่มกาแฟเย็นระหว่างวัน",
            imageResId = R.drawable.product_cup
        ),
        Product(
            id = "gummy-vitc-04",
            nameTh = "วิตามินกัมมี่เคี้ยวเพลิน Bio-C Plus ส้มแท้",
            nameEn = "Bio-C Plus Orange Vitamin Gummies",
            category = "สุขภาพ & ขนม (Health/Snack)",
            priceThb = 189,
            originalPriceThb = 320,
            sellingPoints = listOf(
                "วิตามินซีเข้มข้น + ซิงค์ เสริมภูมิคุ้มกัน ผิวใสออร่า",
                "รสส้มแท้อร่อย เปรี้ยวอมหวาน เคี้ยวหนึบเพลิน ไม่หวานแสบคอ",
                "0% น้ำตาล ใช้สารสกัดหญ้าหวานแท้ ไม่อ้วน ไม่ทำลายฟัน",
                "กินง่าย พกพาสะดวก ทานได้ทั้งครอบครัว"
            ),
            targetAudience = "คนรักสุขภาพ คนที่ไม่ชอบกลืนยาเม็ด และวัยทำงานที่อยากบำรุงผิว",
            imageResId = R.drawable.product_gummy
        )
    )
}
