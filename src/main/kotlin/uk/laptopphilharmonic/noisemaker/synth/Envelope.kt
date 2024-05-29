package uk.laptopphilharmonic.noisemaker.synth

data class Envelope(
    val attack: Int,
    val decay: Int,
    val sustain: Double,
    val release: Int,
) {
    companion object {
        val default = Envelope(
            attack=5, // Reduces the popping sound when a note starts
            decay=0,
            sustain=1.0,
            release=0
        )
    }
}