package uk.laptopphilharmonic.noisemaker.synth

import uk.laptopphilharmonic.noisemaker.frequency.Frequency
import javax.sound.sampled.SourceDataLine
import kotlin.math.pow

interface Synth {
    fun tone(frequency: Frequency, millis: Int, line: SourceDataLine, velocity: Double)
}

abstract class AbstractSynth(
    open val samplesPerSecond: Int,
    bitDepth: Int,
): Synth {
    val bytesPerSample: Int = bitDepth / 8
    val maxVolume = 2.0.pow(((bytesPerSample * 8) - 1).toDouble()) - 1

    override fun tone(frequency: Frequency, millis: Int, line: SourceDataLine, velocity: Double) {
        val toneBuffer = createBuffer(frequency, millis, velocity)
        line.write(toneBuffer, 0, toneBuffer.size)
    }

    /**
     * The pure mathematical wave function for the synth. It should return a value between -1.0 and 1.0.
     * @param sampleIndex - how far through the wave we are
     * @param samplesPerWave - how many samples are in the wave
     */
    abstract fun waveFormSample(sampleIndex: Int, samplesPerWave: Double): Double

    private fun createBuffer(frequency: Frequency, millis: Int, velocity: Double): ByteArray {
        require(velocity in 0.0..1.0)

        val samples = ((millis.toDouble() / 1000.0) * samplesPerSecond.toDouble()).toInt()
        val samplesPerWave = (samplesPerSecond.toDouble() / frequency.hz)
        val bytes = ByteArray(samples * bytesPerSample)
        val velocityFloat = velocity * maxVolume

        for (i in bytes.indices step bytesPerSample) {
            val volume = (waveFormSample(((i / bytesPerSample) % samplesPerWave).toInt(), samplesPerWave) * velocityFloat).toInt()
            for (b in 1..bytesPerSample) {
                bytes[i + bytesPerSample - b] = (volume shr (8 * (b - 1))).toByte()
            }
        }

        return bytes
    }
}