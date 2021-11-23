package nl.frankkie.maxtrix_animations.model

import java.io.File

data class MyFile(val file: File, var frame: Frame? = null) {
    override fun toString(): String {
        return file.name
    }
}