package uk.laptopphilharmonic.noisemaker.synth

import uk.laptopphilharmonic.noisemaker.frequency.Frequency
import kotlin.math.pow
import kotlin.math.sin

class SineSynth(
    override val samplesPerSecond: Int,
    override val bitDepth: Int
) : AbstractSynth(samplesPerSecond, bitDepth) {

    override fun createBuffer(frequency: Frequency, millis: Int, velocity: Double): ByteArray {
        require(velocity in 0.0..1.0)

        val bytesPerSample = bitDepth / 8
        val bytesPerSampleFloat = bytesPerSample.toFloat()
        val samples = ((millis.toDouble() / 1000.0) * samplesPerSecond.toDouble()).toInt()
        val waveLength = (samplesPerSecond.toDouble() / frequency.hz)
        val bytes = ByteArray(samples * bytesPerSample)
        val velocityFloat = velocity * 2.0.pow(((bytesPerSample * 8) - 1).toDouble())

        for (i in bytes.indices step bytesPerSample) {
            val angle = (2 * Math.PI * (i / waveLength)) / bytesPerSample
            val volume = (sin(angle) * velocityFloat).toInt()

            for (b in 1..bytesPerSample) {
                bytes[i + bytesPerSample - b] = (volume shr (8 * (b - 1))).toByte()
            }
        }
        return bytes
    }






//    override fun createBuffer(frequency: Frequency, millis: Int, velocity: Int): ByteArray {
//        require(velocity in 0..127)
//        val byteDepth = bitDepth / 8
//        val output = ByteArray((millis * samplesPerSecond * byteDepth) / 1000)
//        val period = (samplesPerSecond.toDouble() / frequency.hz) * byteDepth
//        for (i in output.indices step byteDepth) {
//            val angle = 2 * Math.PI * (i / period)
//            val volume = (sin(angle) * velocity * byteDepth)
//            for (b in 1..byteDepth) {
//                val a = (volume - (byteDepth * (b - 1))).toInt().coerceAtMost(127)
//                output[i + b - 1] = (a - ((b - 1) * 127)).toByte()
//            }
//        }
//        return output
//    }
}