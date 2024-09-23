package uk.laptopphilharmonic.noisemaker.synth

import uk.laptopphilharmonic.noisemaker.piece.Note
import kotlin.math.sin

/** Basic sine wave (the most fundamental kind in terms of building other sounds) */
class SineSynth : AbstractSynth() {
    override fun waveFormForNoteAtTime(note: Note, timeIntoNote: Double): Double {
        return sin((TWO_PI * timeIntoNote) / note.frequencyAt(timeIntoNote).waveLengthMillis)
    }

    companion object {
        const val TWO_PI = 2 * Math.PI
    }
}