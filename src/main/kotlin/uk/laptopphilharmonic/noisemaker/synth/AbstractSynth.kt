package uk.laptopphilharmonic.noisemaker.synth

import uk.laptopphilharmonic.noisemaker.piece.Note

interface Synth {
    fun volumeForNoteAtTime(note: Note, time: Double): Double
    var envelope: Envelope?
}

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
     * */
    override fun volumeForNoteAtTime(note: Note, time: Double): Double {
        val timeIntoNote = time - note.start
        require(timeIntoNote >= 0) // if you're asking for info on a note that hasn't started you're doing it wrong
        val velocity = note.velocity

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
}