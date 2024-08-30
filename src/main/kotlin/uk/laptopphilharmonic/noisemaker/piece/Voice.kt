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

    fun note(time: Pair<Double, Double>, frequency: Pair<Frequency, Frequency>, velocity: Pair<Double, Double>) {
        this.allNotes.addLast(Note(time, frequency, velocity))
    }

    fun note(time: Pair<Double, Double>, frequency: Frequency, velocity: Pair<Double, Double>) {
        this.allNotes.addLast(Note(time, frequency, velocity))
    }

    fun note(time: Pair<Double, Double>, frequency: Pair<Frequency, Frequency>, velocity: Double) {
        this.allNotes.addLast(Note(time, frequency, velocity))
    }

    fun note(time: Pair<Double, Double>, frequency: Frequency, velocity: Double) {
        this.allNotes.addLast(Note(time, frequency, velocity))
    }

    fun notes(vararg notes: Note) {
        notes.forEach { this.allNotes.addLast(it) }
    }
}