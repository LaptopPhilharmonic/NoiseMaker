package uk.laptopphilharmonic.noisemaker.synth

import uk.laptopphilharmonic.noisemaker.piece.Note
import kotlin.math.sin

class SineSynth : AbstractSynth() {
    override fun waveFormForNoteAtTime(note: Note, timeIntoNote: Double): Double {
        return sin((TWO_PI * timeIntoNote) / note.frequencyAt(timeIntoNote).waveLengthMillis)
    }

    companion object {
        const val TWO_PI = 2 * Math.PI
    }
}