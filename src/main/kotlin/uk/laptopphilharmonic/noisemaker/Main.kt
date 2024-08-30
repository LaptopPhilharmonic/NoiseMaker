package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.frequency.TwelveTone
import uk.laptopphilharmonic.noisemaker.frequency.hz
import uk.laptopphilharmonic.noisemaker.piece.*
import uk.laptopphilharmonic.noisemaker.synth.Envelope
import uk.laptopphilharmonic.noisemaker.synth.SawSynth
import uk.laptopphilharmonic.noisemaker.synth.SquareSynth
import uk.laptopphilharmonic.noisemaker.synth.TriangleSynth

fun main() {
    val nm = NoiseMaker()
    val piece = Piece()
    val v1 = piece.addVoice()
    val v2 = piece.addVoice()
    val tt = TwelveTone()

    val bars = Bars(BarsData(
        beatsPerBar = 4,
        tempo = 120.bpm,
        48
    ))

    v1.synth.envelope = Envelope(0, 0, 1.0, 50)

    bars.withEach {
        v1.notes (
            Note(time = beat(0) to beat(1), frequency = tt.Ab4, velocity = 0.5),
            Note(time = beat(1) to beat(2), frequency = tt.Ab4 to tt.C5, velocity = 0.5),
            Note(time = beat(2) to beat(3), frequency = tt.C5, velocity = 0.5),
            Note(time = beat(3) to endOfBar, frequency = tt.E5, velocity = 0.5 to 0.1)
        )
    }

    nm.play(piece)
}