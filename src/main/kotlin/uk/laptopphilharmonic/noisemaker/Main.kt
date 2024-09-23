package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.frequency.OctaveRange
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
    val beatsPerBar = 4
    val tempo = 360.bpm

    val bars = Bars(BarsData(
        beatsPerBar,
        tempo,
        48
    ))

    v1.synth.envelope = Envelope(0, 0, 1.0, tempo.beatLength.toInt())

    var nextTone = tt.Ab4

    bars.withEach {
        val octave = OctaveRange(tt.Ab4, 1)
        v1.notes (
            Note(time = beat(0) to beat(1), frequency = nextTone.randomOvertone(16).inOctave(octave), velocity = 0.5),
            Note(time = beat(1) to beat(2), frequency = nextTone.randomOvertone(16).inOctave(octave), velocity = 0.5),
            Note(time = beat(2) to beat(3), frequency = nextTone.randomOvertone(16).inOctave(octave), velocity = 0.5),
            Note(time = beat(3) to endOfBar, frequency = nextTone.randomOvertone(16).inOctave(octave), velocity = 0.5 to 0.1)
        )
        nextTone = nextTone.randomOvertone(8).inOctave(octave)
    }

    nm.saveToWav(piece, "test")
}