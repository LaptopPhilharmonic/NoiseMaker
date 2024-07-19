package uk.laptopphilharmonic.noisemaker.piece

import uk.laptopphilharmonic.noisemaker.frequency.Frequency

data class Note(
    /** Note starts this many milliseconds into the piece */
    val start: Int,
    /** Note ends this many milliseconds into the piece */
    val end: Int,
    /** Frequency to play */
    val startFrequency: Frequency,
    /** How hard the note is played. Must be between 0.0 and 1.0 */
    val velocity: Double,
    /** Frequency to slide up to (note stays on startFrequency if unspecified) */
    val endFrequency: Frequency? = null
) {
    /** Not length in milliseconds */
    val duration: Int = end - start

    /** Frequency at supplied number of milliseconds into note */
    fun frequencyAt(timeIntoNote: Double): Frequency {
        return if (endFrequency == null) {
            startFrequency
        } else {
            Frequency(startFrequency.hz + ((timeIntoNote / duration.toDouble()) * (endFrequency.hz - startFrequency.hz)))
        }
    }
}
