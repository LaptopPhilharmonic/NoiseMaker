package uk.laptopphilharmonic.noisemaker.piece

import uk.laptopphilharmonic.noisemaker.frequency.Frequency
import uk.laptopphilharmonic.noisemaker.synth.Synth

class Voice(
    var piece: Piece,
    var synth: Synth,
    var volume: Double = 1.0,
    var pan: Double = 0.0
) {
    val allNotes = mutableListOf<Note>()

    fun note(start: Int, end: Int, startFrequency: Frequency, velocity: Double = 1.0, endFrequency: Frequency? = null) {
        this.allNotes.addLast(Note(start, end, startFrequency, velocity, endFrequency))
    }
}