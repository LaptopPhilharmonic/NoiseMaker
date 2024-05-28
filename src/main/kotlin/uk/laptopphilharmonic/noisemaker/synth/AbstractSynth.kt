package uk.laptopphilharmonic.noisemaker.synth

import uk.laptopphilharmonic.noisemaker.frequency.Frequency
import javax.sound.sampled.SourceDataLine

interface Synth {
    fun tone(frequency: Frequency, millis: Int, line: SourceDataLine, velocity: Double)
}

abstract class AbstractSynth(
    open val samplesPerSecond: Int,
    open val bitDepth: Int,
): Synth {
    abstract fun createBuffer(frequency: Frequency, millis: Int, velocity: Double): ByteArray

    override fun tone(frequency: Frequency, millis: Int, line: SourceDataLine, velocity: Double) {
        val toneBuffer = createBuffer(frequency, millis, velocity)
        line.write(toneBuffer, 0, toneBuffer.size)
    }
}