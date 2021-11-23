import java.io.File
import java.util.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        openUI()
    } else if (args[0] == "help" || args[0] == "-h") {
        printHelp()
    } else {
        processFile(args[0])
    }
}

fun processFile(filename: String) {
    val file = File(filename)
    if (!file.exists()) {
        System.err.println("File does not exist")
        exitProcess(-1)
    }
    if (!file.canRead()) {
        System.err.println("Can't read file")
        exitProcess(-1)
    }
    if (file.isDirectory) {
        file.listFiles().forEach { someFile ->
            val leds = parseFile(someFile)
            leds?.let {
                val base64 = ledsToBase64(leds)
                println(someFile.name)
                println(base64)
            }
        }
    } else {
        val leds = parseFile(File(filename))
        leds?.let {
            val sourceCode = ledsToSourcecode(leds)
            //println(sourceCode)
            val base64 = ledsToBase64(leds)
            println(base64)
        }
    }
}

fun printHelp() {
    print(
        """
        usage: matrix <FILE>
        Transform png image to bytes for arduino
        Needs to be 8x8 rgb png no transparency
    """.trimIndent()
    )
}