package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.snyth.SineSynth
import uk.laptopphilharmonic.noisemaker.snyth.Synth
import javax.sound.sampled.AudioFormat

class NoiseMaker(
    val sampleRate: Int = 32 * 1024,
    val audioFormat: AudioFormat = AudioFormat(sampleRate.toFloat(), 8, 1, true, true)
) {
    fun voice(synth: Synth = SineSynth(), music: Voice.() -> Unit): Voice {
        val voice = Voice(this)
        voice.start()
        voice.music()
        voice.end()
        return voice
    }
}