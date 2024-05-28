package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.frequency.OctaveRange
import uk.laptopphilharmonic.noisemaker.frequency.hz

fun main() {
    val tonic = 220.hz
    val subtonic = (tonic / 3) * 2

    val tonicMajor: Triad = MajorTriad(tonic)
    val subtonicMinor: Triad = MinorTriad(subtonic)
    val dominant: Triad = MajorTriad(tonicMajor.dominant)
    val supertonic: Triad = MajorTriad(MajorTriad(tonicMajor.dominant).dominant.inOctave(OctaveRange(tonic)))
    val superSubtonicMinor: Triad = MinorTriad((subtonic / 3).inOctave(OctaveRange(subtonic)))
    val chords = setOf(tonicMajor, subtonicMinor, dominant)

    val progression = buildList{ for (i in 1..4) { add(chords.random()) } }
    val progressionOctave2 = progression.map { it * 2 }
    val progressionOctave3 = progression.map { it * 4 }
    val progressionOctave4 = progression.map { it * 8 }

    val tempo = 60.bpm

    with(NoiseMaker()) {
        voice(triangleSynth()) {
            for (chord in progression) {
                tone(chord.random(), tempo.beat, 0.5)
            }
        }

        voice() {
            for (chord in progressionOctave2) {
                tone(chord.random(), tempo.halfBeat, 0.5)
                tone(chord.random(), tempo.halfBeat, 0.5)
            }
        }

//        voice {
//            for (chord in progressionOctave3) {
//                tone(chord.random(), tempo.thirdBeat, 0.5)
//                tone(chord.random(), tempo.thirdBeat, 0.5)
//                tone(chord.random(), tempo.thirdBeat, 0.5)
//            }
//        }
//
//        voice {
//            for (chord in progressionOctave2) {
//                tone(chord.random(), tempo.quarterBeat, 0.5)
//                tone(chord.random(), tempo.quarterBeat, 0.5)
//                tone(chord.random(), tempo.quarterBeat, 0.5)
//                tone(chord.random(), tempo.quarterBeat, 0.5)
//            }
//        }
    }
}