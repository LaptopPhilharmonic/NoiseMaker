package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.piece.Piece
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.file.Paths
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

    fun getAsBytes(piece: Piece, byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN): ByteArray {
        val totalSamples = ((piece.length / 1000.0) * sampleRate.toDouble()).toInt()
        val sampleLength = 1000.0 / sampleRate.toDouble()
        val samples = mutableListOf<Double>()
        for (sampleIndex in 0..totalSamples) {
            val millis = (sampleIndex.toDouble() * sampleLength)
            val voicesAndNotes = piece.notesPlayingAtTime(millis)
            samples.addLast(voicesAndNotes.sumOf { it.voice.synth.volumeForNoteAtTime(it.note, millis) })
        }

        val bytes = ByteArray((totalSamples * bytesPerSample) + bytesPerSample)

        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            samples.forEachIndexed { index, sampleVelocity ->
                val volume = (sampleVelocity * maxVolume).toInt()
                val byteIndex = index * bytesPerSample


                for (b in 1..bytesPerSample) {
                    bytes[byteIndex + bytesPerSample - b] = (volume shr (8 * (b - 1))).toByte()
                }
            }
        } else {
            samples.forEachIndexed { index, sampleVelocity ->
                val volume = (sampleVelocity * maxVolume).toInt()
                val byteIndex = index * bytesPerSample


                for (b in 1..bytesPerSample) {
                    bytes[byteIndex + b - 1] = (volume shr (8 * (b - 1))).toByte()
                }
            }
        }

        return bytes
    }

    fun play(piece: Piece) {
        val bytes = getAsBytes(piece)

        with(AudioSystem.getSourceDataLine(audioFormat)) {
            open()
            start()
            write(bytes, 0, bytes.size)
            drain()
            close()
        }
    }

    fun saveToWav(piece: Piece, fileName: String) {
        val pieceBytes = getAsBytes(piece, ByteOrder.BIG_ENDIAN)

        val header = ByteArray(WAV_FILE_HEADER_SIZE)
        val buffer = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN)

        // RIFF Chunk Descriptor
        buffer.put("RIFF".toByteArray()) // Chunk ID
        buffer.putInt(WAV_FILE_HEADER_SIZE + pieceBytes.size - 8) // Chunk Size
        buffer.put("WAVE".toByteArray()) // Format

        // Format sub-chunk
        buffer.put("fmt ".toByteArray())
        buffer.putInt(16) // Sub-chunk size (16 for PCM - Pulse Code Modulation)
        buffer.putShort(1) // Audio Format (1 for PCM)
        buffer.putShort(channels.toShort())
        buffer.putInt(sampleRate)
        buffer.putInt(sampleRate * channels * bitDepth / 8) // Byte Rate
        buffer.putShort((channels * bitDepth / 8).toShort()) // Block Align
        buffer.putShort(bitDepth.toShort()) // Bits per Sample

        // Data sub-chunk
        buffer.put("data".toByteArray())
        buffer.putInt(pieceBytes.size)

        // Write the WAV file
        FileOutputStream("${Paths.get("").toAbsolutePath()}/generated-files/$fileName.wav").use { fos ->
            fos.write(header)
            fos.write(pieceBytes)
        }
    }

    companion object {
        const val SAMPLE_RATE_16 = 16 * 1024
        const val SAMPLE_RATE_32 = 32 * 1024
        const val SAMPLE_RATE_64 = 64 * 1024
        const val SAMPLE_RATE_128 = 128 * 1024

        const val BIT_DEPTH_8 = 8
        const val BIT_DEPTH_16 = 16

        const val WAV_FILE_HEADER_SIZE = 44
    }
}