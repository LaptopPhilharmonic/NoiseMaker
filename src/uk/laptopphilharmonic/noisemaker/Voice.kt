package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.frequency.Frequency
import uk.laptopphilharmonic.noisemaker.synth.Synth
import javax.sound.sampled.AudioSystem

class Voice(
    private val noiseMaker: NoiseMaker,
    var synth: Synth
) {
    private val line = AudioSystem.getSourceDataLine(noiseMaker.audioFormat)

    fun start () {
        line.open(noiseMaker.audioFormat, noiseMaker.sampleRate)
        line.start()
    }

    fun end() {
        line.drain()
        line.close()
    }

    fun tone(frequency: Frequency, millis: Int, velocity: Double) {
        synth.tone(frequency, millis, line, velocity)
    }
}