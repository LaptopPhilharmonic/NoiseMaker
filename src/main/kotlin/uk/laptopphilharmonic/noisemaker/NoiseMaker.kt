package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.piece.Piece
import uk.laptopphilharmonic.noisemaker.synth.StereoVolume
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.file.Paths
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import kotlin.math.pow

/** The main class of this project, for managing piece creation, playback and export to file */
class NoiseMaker(
    /** Samples per second */
    val sampleRate: Int = SAMPLE_RATE_64,
    /** Bits per sample */
    val bitDepth: Int = BIT_DEPTH_16,
    /** i.e. is it mono, stereo, surround sound... */
    val channels: Channels = Channels.Mono,
) {
    val audioFormat: AudioFormat = AudioFormat(sampleRate.toFloat(), bitDepth, channels.count, true, true)
    val bytesPerSample: Int = bitDepth / 8
    val maxVolume = (2.0.pow((bitDepth - 1).toDouble()) - 1).toInt()

    /** A builder method for creating a new Piece */
    fun piece(music: Piece.() -> Unit) {
        val piece = Piece()
        piece.music()
    }

    private fun getAsBytesMono(piece: Piece, byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN): ByteArray {
        val totalSamples = ((piece.length / 1000.0) * sampleRate.toDouble()).toInt()
        val sampleLength = 1000.0 / sampleRate.toDouble()
        val samples = mutableListOf<Double>()
        for (sampleIndex in 0..totalSamples) {
            val millis = (sampleIndex.toDouble() * sampleLength)
            val voicesAndNotes = piece.notesPlayingAtTime(millis)
            samples.addLast(voicesAndNotes.sumOf { it.voice.synth.monoVolumeForNoteAtTime(it.note, millis) })
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

    private fun getAsBytesStereo(piece: Piece, byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN): ByteArray {
        val totalSamples = ((piece.length / 1000.0) * sampleRate.toDouble()).toInt()
        val sampleLength = 1000.0 / sampleRate.toDouble()
        val samples = mutableListOf<StereoVolume>()
        for (sampleIndex in 0..totalSamples) {
            val millis = (sampleIndex.toDouble() * sampleLength)
            val voicesAndNotes = piece.notesPlayingAtTime(millis)
            var leftVolume = 0.0
            var rightVolume = 0.0
            voicesAndNotes.forEach {
                val stereoVolume = it.voice.synth.stereoVolumeForNoteAtTime(it.note, millis)
                leftVolume += stereoVolume.left
                rightVolume += stereoVolume.right
            }
            samples.addLast(StereoVolume(leftVolume, rightVolume))
        }

        val bytes = ByteArray((totalSamples * bytesPerSample * Channels.Stereo.count) + (bytesPerSample * Channels.Stereo.count))

        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            samples.forEachIndexed { index, sampleVelocity ->
                val leftVolume = (sampleVelocity.left * maxVolume).toInt()
                val rightVolume = (sampleVelocity.right * maxVolume).toInt()
                val byteIndex = index * bytesPerSample * Channels.Stereo.count

                for (b in 1..bytesPerSample) {
                    bytes[byteIndex + bytesPerSample - b] = (leftVolume shr (8 * (b - 1))).toByte()
                }

                for (b in 1..bytesPerSample) {
                    bytes[(byteIndex + 2) + bytesPerSample - b] = (rightVolume shr (8 * (b - 1))).toByte()
                }
            }
        } else {
            samples.forEachIndexed { index, sampleVelocity ->
                val leftVolume = (sampleVelocity.left * maxVolume).toInt()
                val rightVolume = (sampleVelocity.right * maxVolume).toInt()
                val byteIndex = index * bytesPerSample * Channels.Stereo.count

                for (b in 1..bytesPerSample) {
                    bytes[byteIndex + b - 1] = (leftVolume shr (8 * (b - 1))).toByte()
                }

                for (b in 1..bytesPerSample) {
                    bytes[(byteIndex + 2) + b - 1] = (rightVolume shr (8 * (b - 1))).toByte()
                }
            }
        }

        return bytes
    }

    /**
     * Play your piece when the code runs
     * @param piece - the piece to be played
     */
    fun play(piece: Piece) {
        val bytes = when(channels) {
            Channels.Mono -> getAsBytesMono(piece)
            Channels.Stereo -> getAsBytesStereo(piece)
        }

        with(AudioSystem.getSourceDataLine(audioFormat)) {
            open()
            start()
            write(bytes, 0, bytes.size)
            drain()
            close()
        }
    }

    /**
     * Export your piece as a raw .wav file
     * @param piece - the piece to export
     * @param fileName - the file name to save it as (in the generated-files directory of the project)
     */
    fun saveToWav(piece: Piece, fileName: String) {
        val pieceBytes = when(channels) {
            Channels.Mono -> getAsBytesMono(piece, ByteOrder.BIG_ENDIAN)
            Channels.Stereo -> getAsBytesStereo(piece, ByteOrder.BIG_ENDIAN)
        }

        val header = ByteArray(WAV_FILE_HEADER_SIZE)
        val buffer = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN)

        // RIFF Chunk Descriptor
        buffer.put("RIFF".toByteArray()) // Chunk ID
        buffer.putInt(WAV_FILE_HEADER_SIZE + pieceBytes.size - 8) // File size
        buffer.put("WAVE".toByteArray()) // Format

        // Format sub-chunk
        buffer.put("fmt ".toByteArray())
        buffer.putInt(16) // Sub-chunk size (16 for PCM - Pulse Code Modulation)
        buffer.putShort(1) // Audio Format (1 for PCM)
        buffer.putShort(channels.count.toShort())
        buffer.putInt(sampleRate)
        buffer.putInt(sampleRate * channels.count * bitDepth / 8) // Byte Rate
        buffer.putShort((channels.count * bitDepth / 8).toShort()) // Block Align
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

enum class Channels(val count: Int) {
    Mono(1),
    Stereo(2)
}