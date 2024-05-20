package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.snyth.SineSynth
import uk.laptopphilharmonic.noisemaker.snyth.Synth
import javax.sound.sampled.AudioSystem

class Voice(
    private val noiseMaker: NoiseMaker,
    var synth: Synth = SineSynth()
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

    fun tone(frequency: Frequency, millis: Int) {
        synth.tone(frequency, millis, line)
    }
}