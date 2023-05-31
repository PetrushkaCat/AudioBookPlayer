package cat.petrushkacat.audiobookplayer.app.util

import org.junit.Assert
import org.junit.Test

class TimeFormatterKtTest {

    @Test
    fun `0`() {
        val result = formatDuration(0)
        Assert.assertEquals("0:00", result)
    }

    @Test
    fun `1000`() {
        val result = formatDuration(1000)
        Assert.assertEquals("0:01", result)
    }

    @Test
    fun `60000`() {
        val result = formatDuration(60000)
        Assert.assertEquals("01:00", result)
    }

    @Test
    fun `61000`() {
        val result = formatDuration(61000)
        Assert.assertEquals("01:01", result)
    }

    @Test
    fun `600000`() {
        val result = formatDuration(600000)
        Assert.assertEquals("10:00", result)
    }

    @Test
    fun `3600000`() {
        val result = formatDuration(3600000)
        Assert.assertEquals("01:00:00", result)
    }

    @Test
    fun `360000000`() {
        val result = formatDuration(360000000)
        Assert.assertEquals("100:00:00", result)
    }
}