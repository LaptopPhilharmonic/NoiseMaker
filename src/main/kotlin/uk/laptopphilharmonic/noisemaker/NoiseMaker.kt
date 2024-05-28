package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.synth.SineSynth
import uk.laptopphilharmonic.noisemaker.synth.Synth
import javax.sound.sampled.AudioFormat
import kotlin.concurrent.thread

class NoiseMaker(
    val sampleRate: Int = SAMPLE_RATE_64,
    val bitDepth: Int = BIT_DEPTH_16,
    val audioFormat: AudioFormat = AudioFormat(sampleRate.toFloat(), bitDepth, 1, true, true)
) {
    fun voice(synth: Synth = sineSynth(), music: Voice.() -> Unit): Voice {
        val voice = Voice(this, synth)
        thread(start = true) {
            voice.start()
            voice.music()
            voice.end()
        }
        return voice
    }

    fun sineSynth() = SineSynth(sampleRate, bitDepth)

    companion object {
        const val SAMPLE_RATE_16 = 16 * 1024
        const val SAMPLE_RATE_32 = 32 * 1024
        const val SAMPLE_RATE_64 = 64 * 1024
        const val SAMPLE_RATE_128 = 128 * 1024

        const val BIT_DEPTH_8 = 8
        const val BIT_DEPTH_16 = 16
    }
}