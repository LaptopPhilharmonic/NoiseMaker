import uk.laptopphilharmonic.noisemaker.NoiseMaker
import uk.laptopphilharmonic.noisemaker.hz

fun main() {
    val tonic = 440.hz

    with(NoiseMaker()) {
        voice {
            tone(tonic, 1000)
        }
    }
}