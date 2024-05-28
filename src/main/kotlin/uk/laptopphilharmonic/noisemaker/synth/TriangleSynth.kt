package uk.laptopphilharmonic.noisemaker.synth

import kotlin.math.sin

class TriangleSynth(
    override val samplesPerSecond: Int,
    bitDepth: Int
) : AbstractSynth(samplesPerSecond, bitDepth) {

    override fun waveFormSample(sampleIndex: Int, samplesPerWave: Double): Double {
        val halfWave = samplesPerWave / 2
        val halfIndex = sampleIndex % (halfWave)
        return if (sampleIndex < halfWave) {
            1 - ((halfIndex / halfWave) * 2)
        } else {
            -1 + ((halfIndex / halfWave) * 2)
        }
    }
}