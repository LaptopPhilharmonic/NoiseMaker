package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.piece.Piece
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import kotlin.math.pow

class NoiseMaker(
    /** Samples per second */
    val sampleRate: Int = SAMPLE_RATE_64,
    /** Bits per sample */
    val bitDepth: Int = BIT_DEPTH_16,
    /** i.e. is it mono, stereo, surround sound... */
    val channels: Int = 1,
) {
    val audioFormat: AudioFormat = AudioFormat(sampleRate.toFloat(), bitDepth, channels, true, true)
    val bytesPerSample: Int = bitDepth / 8
    val maxVolume = (2.0.pow((bitDepth - 1).toDouble()) - 1).toInt()

    fun piece(music: Piece.() -> Unit) {
        val piece = Piece()
        piece.music()
    }

    fun play(piece: Piece) {
        val totalSamples = ((piece.length.toDouble() / 1000.0) * sampleRate.toDouble()).toInt()
        val sampleLength = 1000.0 / sampleRate.toDouble()
        val samples = mutableListOf<Double>()
        for (sampleIndex in 0..totalSamples) {
            val millis = (sampleIndex.toDouble() * sampleLength)
            val voicesAndNotes = piece.notesPlayingAtTime(millis)
            samples.addLast(voicesAndNotes.sumOf { it.voice.synth.volumeForNoteAtTime(it.note, millis) })
        }

        val bytes = ByteArray((totalSamples * bytesPerSample) + bytesPerSample)

        samples.forEachIndexed { index, sampleVelocity ->
            val volume = (sampleVelocity * maxVolume).toInt()
            val byteIndex = index * bytesPerSample

            for (b in 1..bytesPerSample) {
                bytes[byteIndex + bytesPerSample - b] = (volume shr (8 * (b - 1))).toByte()
            }
        }

        with(AudioSystem.getSourceDataLine(audioFormat)) {
            open()
            start()
            write(bytes, 0, bytes.size)
            drain()
            close()
        }
    }

    companion object {
        const val SAMPLE_RATE_16 = 16 * 1024
        const val SAMPLE_RATE_32 = 32 * 1024
        const val SAMPLE_RATE_64 = 64 * 1024
        const val SAMPLE_RATE_128 = 128 * 1024

        const val BIT_DEPTH_8 = 8
        const val BIT_DEPTH_16 = 16
    }
}