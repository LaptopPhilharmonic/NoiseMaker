package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.frequency.OctaveRange
import uk.laptopphilharmonic.noisemaker.frequency.TwelveTone
import uk.laptopphilharmonic.noisemaker.frequency.hz
import uk.laptopphilharmonic.noisemaker.synth.Envelope
import uk.laptopphilharmonic.noisemaker.tempo.bpm

fun main() {
    val tonic = 220.hz
//    val subtonic = (tonic / 3) * 2

//    val tonicMajor: Triad = MajorTriad(tonic)
//    val subtonicMinor: Triad = MinorTriad(subtonic)
//    val dominant: Triad = MajorTriad(tonicMajor.dominant)
//    val supertonic: Triad = MajorTriad(MajorTriad(tonicMajor.dominant).dominant.inOctave(OctaveRange(tonic)))
//    val superSubtonicMinor: Triad = MinorTriad((subtonic / 3).inOctave(OctaveRange(subtonic)))
//    val chords = setOf(tonicMajor, subtonicMinor, dominant)
//
//    val progression = buildList{ for (i in 1..16) { add(chords.random()) } }
//    val progressionOctave2 = progression.map { it * 2 }
//    val progressionOctave3 = progression.map { it * 4 }
//    val progressionOctave4 = progression.map { it * 8 }

    val notes = listOf(tonic, tonic * 3, tonic * 5, tonic * 7, tonic * 11, tonic * 13).map { it.inOctave(OctaveRange(tonic)) }
    val notes2 = notes.map { (it * 3).inOctave(OctaveRange(tonic * 2)) }

    val tempo = 120.bpm
    val velocity = 0.1

    val tt = TwelveTone()

    val testNotes = listOf(tt.A4, tt.Csh5, tt.E5, tt.A5, tt.A4, tt.E4, tt.Csh4, tt.A3)

    with(NoiseMaker()) {
        voice(triangleSynth()) {
            synth.envelope = Envelope(5, 1000, 0.2, 0)
            testNotes.forEach {
                tone(it, tempo.beat, velocity)
            }
        }
//        voice(sineSynth()) {
//            synth.envelope = Envelope.default
//            for (i in 1..32) {
//                tone(notes2.random(), tempo.beat, velocity)
//            }
//        }
    }

//    with(NoiseMaker()) {
//        voice(sineSynth()) {
//            synth.envelope = Envelope.default
//            for (chord in progression) {
//                tone(chord.random(), tempo.beat, velocity)
//            }
//        }
//
//        voice(sineSynth()) {
//            synth.envelope = Envelope.default
//            for (chord in progressionOctave2) {
//                tone(chord.random(), tempo.halfBeat, velocity)
//                tone(chord.random(), tempo.halfBeat, velocity)
//            }
//        }
//
//        voice {
//            synth.envelope = Envelope.default
//            for (chord in progressionOctave3) {
//                tone(chord.random(), tempo.thirdBeat, velocity)
//                tone(chord.random(), tempo.thirdBeat, velocity)
//                tone(chord.random(), tempo.thirdBeat, velocity)
//            }
//        }
//
//        voice {
//            synth.envelope = Envelope.default
//            for (chord in progressionOctave2) {
//                tone(chord.random(), tempo.quarterBeat, velocity)
//                tone(chord.random(), tempo.quarterBeat, velocity)
//                tone(chord.random(), tempo.quarterBeat, velocity)
//                tone(chord.random(), tempo.quarterBeat, velocity)
//            }
//        }
//    }
}