import junit.framework.TestCase

class UtilKtTest : TestCase() {

    fun testBase64ToLeds() {
        //converting from Base64 to leds,
        //and back from leds to Base64 should give same result.
        val pikaBase64 = "AAAAgICAgICAAAAAAAAAAAAAAAAAgICA/4gZAAAAAAAAAAAA/2oA/9gAAAAAAAAAAAAAAAAAAAAA/9gA/9gA/9gA/9gA/4gZAAAA/9gA/9gAAAAA/9gAAAAA/2oA/2oA/2oA/2oAAAAA/wAA/9gA/9gA/9gA/4gZAAAA/4gZ/4gZ/4gZ/9gAAAAA/4gZAAAAAAAA/4gZ/9gA/3kF/9gA/4gZAAAAAAAAAAAA/3kF/2oA/2oA/3kF/9gAAAAAAAAA"
        val leds = base64ToLeds(pikaBase64)
        val backToBase64 = ledsToBase64(leds)
        assert(pikaBase64 == backToBase64)
    }
}