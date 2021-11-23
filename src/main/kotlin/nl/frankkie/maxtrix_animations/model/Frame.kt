package nl.frankkie.maxtrix_animations.model

data class Frame(val leds: List<CRGB>, var duration: Int? = 0, var brightness: Int? = 0)