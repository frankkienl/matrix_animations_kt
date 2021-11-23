package nl.frankkie.maxtrix_animations.ui

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.BorderFactory
import javax.swing.JPanel

class MyImagePanel : JPanel() {
    init {
        border = BorderFactory.createLineBorder(Color.black)
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(250, 200)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        //
        with(State) {
            currentPreviewImage?.let { safeImage ->
                g.drawImage(safeImage, 0, 0, null, null)
            }
        }
    }
}