package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.frequency.hz

fun main() {
    val tonic = 220.hz
    val subtonic = (tonic / 3) * 2

    val tonicMajor: Triad = MajorTriad(tonic)
    val subtonicMinor: Triad = MinorTriad(subtonic)
    val dominant: Triad = MajorTriad(tonicMajor.dominant)
    val chords = setOf(tonicMajor, subtonicMinor, dominant)

    val progression = buildList{ for (i in 1..32) { add(chords.random()) } }
    val progressionOctave2 = progression.map { it * 2 }

    with(NoiseMaker()) {
        voice {
            for(i in 1..32) {
                tone(progression[i - 1].random(), 1000, 0.5)
            }
        }

        voice {
            for(i in 1..32) {
                val chord = progressionOctave2[i - 1]
                tone(chord.random(), 250, 0.5)
                tone(chord.random(), 250, 0.5)
                tone(chord.random(), 250, 0.5)
                tone(chord.random(), 250, 0.5)
            }
        }
    }
}