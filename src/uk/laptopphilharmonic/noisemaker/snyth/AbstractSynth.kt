package uk.laptopphilharmonic.noisemaker.snyth

import uk.laptopphilharmonic.noisemaker.frequency.Frequency
import javax.sound.sampled.SourceDataLine

interface Synth {
    fun tone(frequency: Frequency, millis: Int, line: SourceDataLine)
}

abstract class AbstractSynth(
    open val sampleRate: Int
): Synth {
    abstract fun createBuffer(frequency: Frequency, millis: Int): ByteArray

    override fun tone(frequency: Frequency, millis: Int, line: SourceDataLine) {
        val toneBuffer = createBuffer(frequency, millis)
        line.write(toneBuffer, 0, toneBuffer.size)
    }
}