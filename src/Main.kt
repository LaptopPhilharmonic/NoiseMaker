import uk.laptopphilharmonic.noisemaker.NoiseMaker
import uk.laptopphilharmonic.noisemaker.frequency.OctaveRange
import uk.laptopphilharmonic.noisemaker.frequency.hz

fun main() {
    val tonic = 110.hz
    val octave = OctaveRange(440.hz)

    with(NoiseMaker()) {
        voice {
            for (fundamental in listOf(2.0, 3.0, 2.0, 1.0 / 3.0, 2.0, 3.0, 2.0)) {
                for (i in 2..5) {
                    tone(tonic.overtone(fundamental).overtone(i).inOctave(octave), 500)
                }
            }
        }

        voice {
            for (fundamental in listOf(2.0, 3.0, 2.0, 1.0 / 3.0, 2.0, 3.0, 2.0)) {
                for (i in 5 downTo 2) {
                    tone(tonic.overtone(fundamental).overtone(i).inOctave(octave), 500)
                }
            }
        }
    }
}