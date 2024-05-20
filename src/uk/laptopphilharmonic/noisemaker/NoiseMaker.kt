package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.snyth.SineSynth
import uk.laptopphilharmonic.noisemaker.snyth.Synth
import javax.sound.sampled.AudioFormat
import kotlin.concurrent.thread

class NoiseMaker(
    val sampleRate: Int = 32 * 1024,
    val audioFormat: AudioFormat = AudioFormat(sampleRate.toFloat(), 8, 1, true, true)
) {
    fun voice(synth: Synth = SineSynth(), music: Voice.() -> Unit): Voice {
        val voice = Voice(this, synth)
        thread(start = true) {
            voice.start()
            voice.music()
            voice.end()
        }
        return voice
    }
}