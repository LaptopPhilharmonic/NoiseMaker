package uk.laptopphilharmonic.noisemaker.synth

import uk.laptopphilharmonic.noisemaker.piece.Note

/** A triangle shaped wave with no curviness to it */
class TriangleSynth : AbstractSynth() {
    override fun waveFormForNoteAtTime(note: Note, timeIntoNote: Double): Double {
        val currentFrequency = note.frequencyAt(timeIntoNote)
        val halfWave = currentFrequency.waveLengthMillis / 2
        val timeIntoHalfWave = timeIntoNote % halfWave
        return if (timeIntoNote % currentFrequency.waveLengthMillis < halfWave) {
            1 - ((timeIntoHalfWave / halfWave) * 2)
        } else {
            - 1 + ((timeIntoHalfWave / halfWave) * 2)
        }
    }
}