package uk.laptopphilharmonic.noisemaker.synth

import uk.laptopphilharmonic.noisemaker.frequency.Frequency
import javax.sound.sampled.SourceDataLine
import kotlin.math.pow

interface Synth {
    fun tone(frequency: Frequency, millis: Int, line: SourceDataLine, velocity: Double)
    var envelope: Envelope?
}

abstract class AbstractSynth(
    open val samplesPerSecond: Int,
    bitDepth: Int,
): Synth {
    val bytesPerSample: Int = bitDepth / 8
    val maxVolume = 2.0.pow(((bytesPerSample * 8) - 1).toDouble()) - 1
    override var envelope: Envelope? = null

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
        val samplesPerWave = (samplesPerSecond.toDouble() / frequency.hz) * 8 // Why is it times 8? Need to figure this out
        val bytes = ByteArray(samples * bytesPerSample)
        val millisPerSample = millis.toDouble() / samples.toDouble()
        val velocityFloat = velocity * maxVolume

        // We take a snapshot of these when the wave starts to avoid any confusion if the envelope is changed later
        val attack = (envelope?.attack ?: 0).toDouble()
        val decay = (envelope?.decay ?: 0).toDouble()
        val decayEnd = attack + decay
        val sustainVelocity = (envelope?.sustain ?: 1.0) * velocityFloat
        val sustainVelocityDiff = velocityFloat - sustainVelocity
        val release = (envelope?.release ?: 0).toDouble()

        for (i in bytes.indices step bytesPerSample) {
            val sampleIndex = i / bytesPerSample
            val currentMillis = millisPerSample * sampleIndex
            val envelopeVelocity = if (envelope != null) {
                if (currentMillis < attack) {
                    velocityFloat * (currentMillis / attack)
                } else if (currentMillis < decayEnd) {
                    velocityFloat - (sustainVelocityDiff * ((currentMillis - attack) / decayEnd))
                } else {
                    sustainVelocity
                }
            } else {
                velocityFloat
            }

            var volume = (waveFormSample((sampleIndex % samplesPerWave).toInt(), samplesPerWave) * envelopeVelocity).toInt()

            // Without this you'll get horrible popping at the end of every note
            val endProximity = millis - currentMillis
            if (endProximity < 5) {
                volume = (volume * (endProximity / 5)).toInt()
            }

            for (b in 1..bytesPerSample) {
                bytes[i + bytesPerSample - b] = (volume shr (8 * (b - 1))).toByte()
            }
        }

        return bytes
    }
}