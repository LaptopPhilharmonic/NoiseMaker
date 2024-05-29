package uk.laptopphilharmonic.noisemaker.synth

class SawSynth(
    override val samplesPerSecond: Int,
    bitDepth: Int
) : AbstractSynth(samplesPerSecond, bitDepth) {

    override fun waveFormSample(sampleIndex: Int, samplesPerWave: Double): Double {
        return 1 - (2 * (sampleIndex / samplesPerWave))
    }
}