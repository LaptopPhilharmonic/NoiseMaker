package uk.laptopphilharmonic.noisemaker.synth

class SquareSynth(
    override val samplesPerSecond: Int,
    bitDepth: Int
) : AbstractSynth(samplesPerSecond, bitDepth) {

    override fun waveFormSample(sampleIndex: Int, samplesPerWave: Double): Double {
        return if (sampleIndex < samplesPerWave / 2) {
            1.0
        } else {
            -1.0
        }
    }
}