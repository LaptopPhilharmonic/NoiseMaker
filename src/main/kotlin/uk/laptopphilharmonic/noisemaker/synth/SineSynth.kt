package uk.laptopphilharmonic.noisemaker.synth

import kotlin.math.sin

class SineSynth(
    override val samplesPerSecond: Int,
    bitDepth: Int
) : AbstractSynth(samplesPerSecond, bitDepth) {

    override fun waveFormSample(sampleIndex: Int, samplesPerWave: Double): Double {
        return sin(TWO_PI * sampleIndex / samplesPerWave)
    }

    companion object {
        const val TWO_PI = 2 * Math.PI
    }
}