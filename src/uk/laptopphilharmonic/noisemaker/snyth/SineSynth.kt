package uk.laptopphilharmonic.noisemaker.snyth

import uk.laptopphilharmonic.noisemaker.frequency.Frequency
import kotlin.math.sin

class SineSynth(override val sampleRate: Int = 32 * 1024) : AbstractSynth(sampleRate) {
    override fun createBuffer(frequency: Frequency, millis: Int): ByteArray {
        val output = ByteArray((millis * sampleRate) / 1000)
        val period = sampleRate.toDouble() / frequency.hz
        for (i in output.indices) {
            val angle = 2.0 * Math.PI * i / period
            output[i] = (sin(angle) * 127f).toInt().toByte()
        }
        return output
    }
}