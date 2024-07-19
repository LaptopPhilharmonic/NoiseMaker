package uk.laptopphilharmonic.noisemaker.synth

import uk.laptopphilharmonic.noisemaker.piece.Note

class SquareSynth : AbstractSynth() {
    override fun waveFormForNoteAtTime(note: Note, timeIntoNote: Double): Double {
        val currentFrequency = note.frequencyAt(timeIntoNote)
        return if (timeIntoNote % currentFrequency.waveLengthMillis < currentFrequency.waveLengthMillis / 2) {
            1.0
        } else {
            -1.0
        }
    }
}