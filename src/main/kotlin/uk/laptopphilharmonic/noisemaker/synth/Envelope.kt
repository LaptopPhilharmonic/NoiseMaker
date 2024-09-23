package uk.laptopphilharmonic.noisemaker.synth

/**
 * An Envelope describes the attack, decay, sustain and release times and volumes for notes played by a Synth
 * @param attack - how many milliseconds to fade a note in from silence for
 * @param decay - when the attack is complete, how long it takes for the volume to change to whatever the sustain volume is
 * @param sustain - what volume to play the note at while it is held after the attack and decay periods above
 * @param release - how long the note takes to fade to silence after being released
 */
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