package uk.laptopphilharmonic.noisemaker.synth

import uk.laptopphilharmonic.noisemaker.piece.Note

class SawSynth : AbstractSynth() {
    override fun waveFormForNoteAtTime(note: Note, timeIntoNote: Double): Double {
        val currentFrequency = note.frequencyAt(timeIntoNote)
        return 1 - (2 * ((timeIntoNote % currentFrequency.waveLengthMillis) / currentFrequency.waveLengthMillis))
    }
}