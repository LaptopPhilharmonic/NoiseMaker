package uk.laptopphilharmonic.noisemaker.synth

import uk.laptopphilharmonic.noisemaker.piece.Note
import kotlin.math.abs

/** A Synthesiser is a fun tool for creating different electronic instruments by generating wave forms */
interface Synth {
    /**
     * Get the volume for a note played by this synthesiser at a particular time into the piece. The volume returned
     * will be between 0.0 (silence) to 1.0 (max volume)
     * @param note - the Note in question
     * @param time - how far into the piece we are in milliseconds
     */
    fun monoVolumeForNoteAtTime(note: Note, time: Double): Double

    /**
     * Get the left and right channel volume for a note played by this synthesiser at a particular time into the piece.
     * The volume returned for each channel will be between 0.0 (silence) to 1.0 (max volume)
     * @param note - the Note in question
     * @param time - how far into the piece we are in milliseconds
     */
    fun stereoVolumeForNoteAtTime(note: Note, time: Double): StereoVolume

    /**
     * You can assign an Envelope to a synthesizer to control the volumes and timings of the attack, decay, sustain
     * and release of notes played
     */
    var envelope: Envelope?
}

/** A class for all Synths to inherit from. */
abstract class AbstractSynth: Synth {
    override var envelope: Envelope? = null

    /**
     * The pure mathematical wave function for the synth. It should return a value between -1.0 and 1.0.
     * @param note - the note played
     * @param timeIntoNote - how far into the note we are in milliseconds
     */
    abstract fun waveFormForNoteAtTime(note: Note, timeIntoNote: Double): Double

    /**
     * Returns volume for this note at the time specified as -1 to 1 double
     * @param note - the note in question
     * @param time - how many milliseconds into the piece we are
     */
    override fun monoVolumeForNoteAtTime(note: Note, time: Double): Double {
        val timeIntoNote = time - note.startTime
        require(timeIntoNote >= 0) // if you're asking for info on a note that hasn't started you're doing it wrong
        val velocity = note.velocityAt(timeIntoNote)

        val envelopeVelocity: Double = if (envelope != null) {
            val sustain = envelope?.sustain ?: 1.0
            val sustainVelocity = velocity * sustain
            val sustainDiff = sustainVelocity - velocity
            val attack = envelope!!.attack
            val decay = envelope!!.decay
            val release = envelope!!.release

            if (timeIntoNote < attack) {
                velocity * (timeIntoNote / attack)
            } else if (timeIntoNote < attack + decay) {
                (sustainVelocity) + (sustainDiff * (1.0 - ((timeIntoNote - attack) / decay)))
            } else if (timeIntoNote > note.duration) {
                (sustainVelocity) * (1.0 - ((timeIntoNote - note.duration) / release))
            } else {
                sustainVelocity
            }
        } else {
            velocity
        }

        var volume = (waveFormForNoteAtTime(note, timeIntoNote) * envelopeVelocity)

        if (envelope == null || envelope!!.release < 5) {
            // Without this you'll get horrible popping at the end of every note
            val endProximity = note.duration - timeIntoNote
            if (endProximity < 5) {
                volume = (volume * (endProximity / 5.0))
            }
        }

        return volume
    }

    /**
     * Returns volume for this note at the time specified as -1 to 1 double for left and right channels of stereo
     * @param note - the note in question
     * @param time - how many milliseconds into the piece we are
     */
    override fun stereoVolumeForNoteAtTime(note: Note, time: Double): StereoVolume {
        val timeIntoNote = time - note.startTime
        val totalVolume = monoVolumeForNoteAtTime(note, time)
        val leftPercent = abs((note.panAt(timeIntoNote)) - 1.0) / 2.0
        val rightPercent = abs((note.panAt(timeIntoNote)) + 1.0) / 2.0
        return StereoVolume(
            left = totalVolume * leftPercent,
            right = totalVolume * rightPercent,
        )
    }
}

/** Handles stereo volume */
data class StereoVolume(
    val left: Double,
    val right: Double
)