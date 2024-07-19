package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.frequency.hz
import uk.laptopphilharmonic.noisemaker.piece.Piece
import uk.laptopphilharmonic.noisemaker.synth.Envelope

fun main() {
    val nm = NoiseMaker()
    val piece = Piece()
    val v1 = piece.addVoice()
    val v2 = piece.addVoice()

    v1.synth.envelope = Envelope(100, 200, 1.0, 500)

    v1.note(0, 4000, 440.hz, 0.2)
    v1.note(0, 500, 550.hz, 0.2)
    v1.note(500, 1000, 660.hz, 0.2)
    v1.note(1000, 1500, 550.hz, 0.2)
    v1.note(1500, 2000, 660.hz, 0.2)
    v1.note(2000, 2500, 550.hz, 0.2)
    v1.note(2500, 5000, 770.hz, 0.2)

    nm.play(piece)
}