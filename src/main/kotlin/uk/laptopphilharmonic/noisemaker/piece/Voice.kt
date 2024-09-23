package uk.laptopphilharmonic.noisemaker.piece

import uk.laptopphilharmonic.noisemaker.frequency.Frequency
import uk.laptopphilharmonic.noisemaker.synth.Synth

/** A Voice that plays Notes based on the setting supplied */
class Voice(
    /** The Piece this Voice is part of */
    var piece: Piece,
    /** What Synth to play notes with */
    var synth: Synth,
    /** How loud to play Notes in this Voice (0.0 = silent, 1.0 = max) */
    var volume: Double = 1.0,
    /** Where to play Notes in this Voice if it's a stereo mix (-1.0 = full left, 1.0 = full right) */
    var pan: Double = 0.0
) {
    val allNotes = mutableListOf<Note>()

    /**
     * Add a new Note for this Voice to play as part of the Piece it belongs to
     * @param time when to play this note to and from in milliseconds (e.g. 100 to 200)
     * @param frequency what frequency to start and end the note on (e.g. 440.hz to 880.hz)
     * @param velocity how loud to start this note and how loud to end it (e.g. 0.25 to 0.75)
     */
    fun note(time: Pair<Double, Double>, frequency: Pair<Frequency, Frequency>, velocity: Pair<Double, Double>) {
        this.allNotes.addLast(Note(time, frequency, velocity))
    }

    /**
     * Add a new note for this Voice to play as part of the Piece it belongs to
     * @param time when to play this note to and from in milliseconds (e.g. 100 to 200)
     * @param frequency what frequency to play the note at (e.g. 400.hz)
     * @param velocity how loud to start this note and how loud to end it (e.g. 0.25 to 0.75)
     */
    fun note(time: Pair<Double, Double>, frequency: Frequency, velocity: Pair<Double, Double>) {
        this.allNotes.addLast(Note(time, frequency, velocity))
    }

    /**
     * Add a new Note for this Voice to play as part of the Piece it belongs to
     * @param time when to play this note to and from in milliseconds (e.g. 100 to 200)
     * @param frequency what frequency to start and end the note on (e.g. 440.hz to 880.hz)
     * @param velocity how loud to play this note (0.0 = silent, 1.0 = max)
     */
    fun note(time: Pair<Double, Double>, frequency: Pair<Frequency, Frequency>, velocity: Double) {
        this.allNotes.addLast(Note(time, frequency, velocity))
    }

    /**
     * Add a new Note for this Voice to play as part of the Piece it belongs to
     * @param time when to play this note to and from in milliseconds (e.g. 100 to 200)
     * @param frequency what frequency to play the note at (e.g. 400.hz)
     * @param velocity how loud to play this note (0.0 = silent, 1.0 = max)
     */
    fun note(time: Pair<Double, Double>, frequency: Frequency, velocity: Double) {
        this.allNotes.addLast(Note(time, frequency, velocity))
    }

    /**
     * Add several Notes (accepted as a vararg)
     */
    fun notes(vararg notes: Note) {
        notes.forEach { this.allNotes.addLast(it) }
    }
}