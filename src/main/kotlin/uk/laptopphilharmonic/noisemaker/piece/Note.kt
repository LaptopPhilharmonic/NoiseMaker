package uk.laptopphilharmonic.noisemaker.piece

import uk.laptopphilharmonic.noisemaker.frequency.Frequency

data class Note(
    /** Pair representing the start and end times of the note */
    private val time: Pair<Double, Double>,
    /** Pair representing the start and end frequencies of the note */
    private val frequency: Pair<Frequency, Frequency?>,
    /** Pair representing the start and end velocities of the note */
    private val velocity: Pair<Double, Double?>,
) {

    /** Alternative constructor for when frequency doesn't change */
    constructor(
        /** Pair representing the start and end times of the note */
        time: Pair<Double, Double>,
        /** The frequency sustained for the duration of the note */
        frequency: Frequency,
        /** Pair representing the start and end velocities of the note */
        velocity: Pair<Double, Double>
    ): this(
        time, frequency to null, velocity
    )

    /** Alternative constructor for when velocity doesn't change */
    constructor(
        /** Pair representing the start and end times of the note */
        time: Pair<Double, Double>,
        /** Pair representing the start and end frequencies of the note */
        frequency: Pair<Frequency, Frequency>,
        /** The velocity sustained for the duration of the note */
        velocity: Double
    ): this (
        time, frequency, velocity to null
    )

    /** Alternative constructor for when neither frequency nor velocity changes*/
    constructor(
        /** Pair representing the start and end times of the note */
        time: Pair<Double, Double>,
        /** The frequency sustained for the duration of the note */
        frequency: Frequency,
        /** The velocity sustained for the duration of the note */
        velocity: Double
    ): this (
        time, frequency to null,  velocity to null
    )

    /** Frequency to play */
    val startFrequency: Frequency = frequency.first
    /** Frequency to slide to (note stays on startFrequency if unspecified) */
    val endFrequency: Frequency? = frequency.second
    /** Note starts this many milliseconds into the piece */
    val startTime: Double = time.first
    /** Note ends this many milliseconds into the piece */
    val endTime: Double = time.second
    /** How hard the note is played at the start. Must be between 0.0 and 1.0 */
    val startVelocity: Double = velocity.first
    /** How hard the note is played at the end. Must be between 0.0 and 1.0 */
    val endVelocity: Double? = velocity.second
    /** Note length in milliseconds */
    val duration: Double = endTime - startTime

    /** Frequency at supplied number of milliseconds into note */
    fun frequencyAt(timeIntoNote: Double): Frequency {
        return if (endFrequency == null) {
            startFrequency
        } else {
            Frequency(startFrequency.hz + ((timeIntoNote / duration) * (endFrequency.hz - startFrequency.hz)))
        }
    }

    /** Velocity at supplied number of milliseconds into note */
    fun velocityAt(timeIntoNote: Double): Double {
        return if (endVelocity == null) {
            startVelocity
        } else {
            startVelocity + ((timeIntoNote / duration) * (endVelocity - startVelocity))
        }
    }
}