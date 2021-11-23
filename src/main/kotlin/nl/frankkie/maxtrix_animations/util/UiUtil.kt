package nl.frankkie.maxtrix_animations.util

import java.awt.BasicStroke
import java.awt.Color
import java.awt.image.BufferedImage

fun scaleUpImage(image: BufferedImage, scale: Int): BufferedImage {
    val temp = BufferedImage(8 * scale, 8 * scale, BufferedImage.TYPE_INT_ARGB)
    val g = temp.createGraphics()
    //pixels
    for (y in 0..7) {
        for (x in 0..7) {
            val scaledY = y * scale
            val scaledX = x * scale
            g.color = Color(image.getRGB(x, y))
            g.fillRect(scaledX, scaledY, scale, scale)
        }
    }
    return temp
}

fun drawLinesOnPreviewImage(image: BufferedImage, scale: Int, type: PreviewLines) : BufferedImage {
    return when (type) {
        PreviewLines.STYLE1 -> drawLinesOnPreviewImage1(image, scale)
        PreviewLines.STYLE2 -> drawLinesOnPreviewImage2(image, scale)
        else -> image
    }
}

fun drawLinesOnPreviewImage1(image: BufferedImage, scale: Int): BufferedImage {
    val g = image.createGraphics()
    //lines
    for (i in 0..7) {
        val scaled = i * scale
        g.color = Color.BLACK
        g.stroke = BasicStroke(5f)
        g.drawLine(0, scaled, 8 * scale, scaled)
        g.drawLine(scaled, 0, scaled, 8 * scale)
    }
    return image
}

fun drawLinesOnPreviewImage2(image: BufferedImage, scale: Int): BufferedImage {
    val g = image.createGraphics()
    //lines
    for (i in 0..7) {
        val scaled = i * scale
        g.stroke = BasicStroke(1f)
        g.color = Color.BLACK
        g.drawLine(0, scaled, 8 * scale, scaled)
        g.drawLine(scaled, 0, scaled, 8 * scale)
        //
        g.stroke = BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0f, floatArrayOf(5f), 0f)
        g.color = Color.WHITE
        g.drawLine(0, scaled, 8 * scale, scaled)
        g.drawLine(scaled, 0, scaled, 8 * scale)
    }
    return image
}