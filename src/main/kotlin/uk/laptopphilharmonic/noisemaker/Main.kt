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
    val nm = NoiseMaker(channels = Channels.Stereo)
    val piece = Piece()
    val v1 = piece.addVoice()
    val v2 = piece.addVoice()
    val tt = TwelveTone()
    val beatsPerBar = 4
    val tempo = 240.bpm

    val bars = Bars(BarsData(
        beatsPerBar,
        tempo,
        1
    ))

    v1.synth.envelope = Envelope(0.5, 0.0, 1.0, 0.0)

    v1.note(0.0 to 1000.0, 440.hz, 0.5, -1.0 to -1.0)
    v1.note(1000.0 to 2000.0, 440.hz, 0.5, 1.0 to 1.0)
    v1.note(2000.0 to 3000.0, 440.hz, 0.5, 0.0 to 0.0)
    v1.note(3000.0 to 4000.0, 440.hz, 0.5, -1.0 to 1.0)

//
//    var nextTone = 357.hz
//    val range = OctaveRange(nextTone, 2)
//
//    bars.withEach {
//        listOf(0.5, 3.0, 5.0, 8.0).map {
//            (nextTone * it).inOctave(range)
//        }.forEachIndexed { i, tone ->
//            v1.note(
//                time = beat(i) to beat(i + 0.1),
//                frequency = tone,
//                velocity = 0.2,
//                pan = -1.0 to null
//            )
//            v1.note(
//                time = beat(i + 0.3333333) to beat(i + 0.1),
//                frequency = tone * 2,
//                velocity = 0.05,
//                pan = 1.0 to null
//            )
//            v1.note(
//                time = beat(i + 0.6666666) to beat(i + 0.1),
//                frequency = tone / 2,
//                velocity = 0.025,
//                pan = -0.5 to 0.5
//            )
//        }
//
//        listOf(0.5, 3.0, 5.0, 9.0).map {
//            (nextTone / 3 * it).inOctave(range)
//        }.forEachIndexed { i, tone ->
//            v1.note(
//                time = beat(i + 0.5) to beat(i + 0.1),
//                frequency = tone,
//                velocity = 0.1
//            )
//        }
//
//        listOf(0.5, 3.0, 5.0, 9.0).map {
//            (nextTone / 2 * it).inOctave(range)
//        }.forEachIndexed { i, tone ->
//            v1.note(
//                time = beat(i + 0.75) to beat(i + 0.1),
//                frequency = tone,
//                velocity = 0.05
//            )
//        }
//
//        nextTone = (nextTone / (1 + (number % 6))).inOctave(range)
//    }

   nm.saveToWav(piece, "a4-pantest")

    // nm.play(piece)
}