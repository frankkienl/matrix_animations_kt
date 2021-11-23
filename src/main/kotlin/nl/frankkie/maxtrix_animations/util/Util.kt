package nl.frankkie.maxtrix_animations

import nl.frankkie.maxtrix_animations.model.CRGB
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

fun ledsToSourcecode(leds: List<CRGB>): String {
    val sb = StringBuilder()
    for (i in 0..63) {
        val led = leds[i]
        sb.append("  leds[$i] = CRGB(${led.r},${led.g},${led.b});\n")
    }
    return sb.toString()
}

fun ledsToBase64(leds: List<CRGB>): String {
    val bytes = mutableListOf<Byte>()
    for (i in 0..63) {
        val led = leds[i]
        bytes.add(led.r.toByte())
        bytes.add(led.g.toByte())
        bytes.add(led.b.toByte())
    }
    val byteArray = bytes.toByteArray()
    val base64String = Base64.getEncoder().encodeToString(byteArray)
    return base64String
}

fun base64ToLeds(base64: String): List<CRGB> {
    val byteArray = Base64.getDecoder().decode(base64)
    val leds = mutableListOf<CRGB>()
    for (i in 0 until 64 * 3 step 3) {
        val led = CRGB(
            byteArray[i].toInt(),
            byteArray[i + 1].toInt(),
            byteArray[i + 2].toInt()
        )
        leds.add(led)
    }
    return leds
}

fun parseFile(file: File): List<CRGB>? {
    val bufferedImage = ImageIO.read(file) ?: return null
    val leds = mutableListOf<CRGB>()
    //fill with black
    for (ii in 1..64) {
        leds.add(CRGB(0, 0, 0))
    }
    for (y in 0..7) {
        for (x in 0..7) {
            val rgbInt = bufferedImage.getRGB(x, y)
            val color = Color(rgbInt)
            leds[xy(x, y)].r = color.red
            leds[xy(x, y)].g = color.green
            leds[xy(x, y)].b = color.blue
        }
    }
    return leds
}

fun xy(x: Int, y: Int): Int {
    val MATRIX_WIDTH = 8
    val i = if ((y and 0x01 == 1)) {
        // Odd rows run backwards
        val reverseX: Int = MATRIX_WIDTH - 1 - x
        y * MATRIX_WIDTH + reverseX
    } else {
        // Even rows run forwards
        y * MATRIX_WIDTH + x
    }
    return i
}

fun fileToImage(file: File): BufferedImage {
    return ImageIO.read(file)
}