package uk.laptopphilharmonic.noisemaker.piece

import uk.laptopphilharmonic.noisemaker.frequency.Frequency
import kotlin.math.abs

data class Note(
    /** Pair representing the start and end times of the note */
    private val time: Pair<Double, Double>,
    /** Pair representing the start and end frequencies of the note */
    private val frequency: Pair<Frequency, Frequency?>,
    /** Pair representing the start and end velocities of the note */
    private val velocity: Pair<Double, Double?>,
    /** Pair representing start and end pan of the note (-1.0 = full left, 1.0 = full right).
     * Ignored if not in stereo mode. Defaults to centre. */
    private val pan: Pair<Double, Double?> = 0.0 to 0.0
) {

    /** Alternative constructor for when frequency doesn't change */
    constructor(
        /** Pair representing the start and end times of the note */
        time: Pair<Double, Double>,
        /** The frequency sustained for the duration of the note */
        frequency: Frequency,
        /** Pair representing the start and end velocities of the note */
        velocity: Pair<Double, Double>,
        /** Pair representing the start and end pan of the note */
        pan: Pair<Double, Double?> = 0.0 to null
    ): this(
        time, frequency to null, velocity, pan
    )

    /** Alternative constructor for when velocity doesn't change */
    constructor(
        /** Pair representing the start and end times of the note */
        time: Pair<Double, Double>,
        /** Pair representing the start and end frequencies of the note */
        frequency: Pair<Frequency, Frequency>,
        /** The velocity sustained for the duration of the note */
        velocity: Double,
        /** Pair representing the start and end pan of the note */
        pan: Pair<Double, Double?> = 0.0 to null
    ): this (
        time, frequency, velocity to null, pan
    )

    /** Alternative constructor for when pan doesn't change */
    constructor(
        /** Pair representing the start and end times of the note */
        time: Pair<Double, Double>,
        /** Pair representing the start and end frequencies of the note */
        frequency: Pair<Frequency, Frequency>,
        /** Pair representing the start and end velocities of the note */
        velocity: Pair<Double, Double>,
        /** Pair representing the start and end pan of the note */
        pan: Double = 0.0
    ): this(
        time, frequency, velocity, pan to null
    )

    /** Alternative constructor for when neither frequency nor velocity changes */
    constructor(
        /** Pair representing the start and end times of the note */
        time: Pair<Double, Double>,
        /** The frequency sustained for the duration of the note */
        frequency: Frequency,
        /** The velocity sustained for the duration of the note */
        velocity: Double,
        /** Pair representing the start and end pan of the note */
        pan: Pair<Double, Double?> = 0.0 to null
    ): this (
        time, frequency to null,  velocity to null, pan
    )

    /** Alternative constructor for when frequency and pan don't change */
    constructor(
        /** Pair representing the start and end times of the note */
        time: Pair<Double, Double>,
        /** The frequency sustained for the duration of the note */
        frequency: Frequency,
        /** Pair representing the start and end velocities of the note */
        velocity: Pair<Double, Double>,
        /** The pan (left or right) of the note - ignored if not in stereo mode. */
        pan: Double = 0.0
    ): this(
        time, frequency to null, velocity, pan to null
    )

    /** Alternative constructor for when velocity and pan don't change */
    constructor(
        /** Pair representing the start and end times of the note */
        time: Pair<Double, Double>,
        /** Pair representing the start and end frequencies of the note */
        frequency: Pair<Frequency, Frequency>,
        /** The velocity sustained for the duration of the note */
        velocity: Double,
        /** The pan (left or right) of the note - ignored if not in stereo mode. */
        pan: Double = 0.0
    ): this (
        time, frequency, velocity to null, pan to null
    )

    /** Alternative constructor for when neither frequency nor velocity nor pan changes */
    constructor(
        /** Pair representing the start and end times of the note */
        time: Pair<Double, Double>,
        /** The frequency sustained for the duration of the note */
        frequency: Frequency,
        /** The velocity sustained for the duration of the note */
        velocity: Double,
        /** The pan (left or right) of the note - ignored if not in stereo mode. */
        pan: Double = 0.0
    ): this (
        time, frequency to null,  velocity to null, pan to null
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
    /** In stereo mode, where is the note panned to at the start? (-1.0 = full left, 1.0 = full right) */
    val startPan: Double = pan.first
    /** In stereo mode, where is the note panned to at the end? (-1.0 = full left, 1.0 = full right) */
    val endPan: Double? = pan.second
    /** Note length in milliseconds */
    val duration: Double = endTime - startTime

    private val panDirection = if (endPan != null && startPan > endPan) -1 else 1

    /** Frequency at supplied number of milliseconds into note */
    fun frequencyAt(timeIntoNote: Double): Frequency {
        return if (endFrequency == null || startFrequency == endFrequency) {
            startFrequency
        } else {
            Frequency(startFrequency.hz + ((timeIntoNote / duration) * (endFrequency.hz - startFrequency.hz)))
        }
    }

    /** Velocity at supplied number of milliseconds into note */
    fun velocityAt(timeIntoNote: Double): Double {
        return if (endVelocity == null || startVelocity == endVelocity) {
            startVelocity
        } else {
            startVelocity + ((timeIntoNote / duration) * (endVelocity - startVelocity))
        }
    }

    /** Pan at supplied number of milliseconds into note */
    fun panAt(timeIntoNote: Double): Double {
        return if (endPan == null || endPan == startPan) {
            startPan
        } else {
            startPan + ((timeIntoNote / duration) * abs(endPan - startPan) * panDirection)
        }
    }
}