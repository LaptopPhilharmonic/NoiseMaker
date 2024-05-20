import uk.laptopphilharmonic.noisemaker.NoiseMaker
import uk.laptopphilharmonic.noisemaker.hz

fun main() {
    val tonic = 440.hz

    with(NoiseMaker()) {
        voice {
            for (fundamental in 3..5) {
                for (i in 1..5) {
                    tone(tonic.overtone(fundamental).overtone(i).undertone(4), 500)
                }
            }
        }

        voice {
            for (fundamental in 3..5) {
                for (i in 5 downTo 1) {
                    tone(tonic.overtone(fundamental).overtone(i).undertone(4), 500)
                }
            }
        }
    }
}