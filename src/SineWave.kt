import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.SourceDataLine
import kotlin.concurrent.thread
import kotlin.math.sin

object SinSynth {
    private const val SAMPLE_RATE: Int = 32 * 1024

    fun createSinWaveBuffer(freq: Double, ms: Int): ByteArray {
        val output = ByteArray((ms * SAMPLE_RATE) / 1000)
        val period = SAMPLE_RATE.toDouble() / freq
        for (i in output.indices) {
            val angle = 2.0 * Math.PI * i / period
            output[i] = (sin(angle) * 127f).toInt().toByte()
        }
        return output
    }

    fun playTone(freq: Double, ms: Int, line: SourceDataLine) {
        val toneBuffer = createSinWaveBuffer(freq, ms)
        line.write(toneBuffer, 0, toneBuffer.size)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val af = AudioFormat(SAMPLE_RATE.toFloat(), 8, 1, true, true)

        val tonic = 220.0

        thread(start = true) {
                val line2 = AudioSystem.getSourceDataLine(af)
                line2.open(af, SAMPLE_RATE)
                line2.start()

                for (i in 1..10) {
                    line2.playTone(tonic * i / 2 , 600)
                }

                line2.drain()
                line2.close()
        }
        thread(start = true) {
            val line1 = AudioSystem.getSourceDataLine(af)
            line1.open(af, SAMPLE_RATE)
            line1.start()

            for (i in 1..10) {
                val time = 100
                line1.playTone(tonic * i, time)
                line1.playTone(tonic * i * 1.25, time)
                line1.playTone(tonic * i * 1.5, time)
                line1.playTone(tonic * i * 1.5 , time)
                line1.playTone(tonic * i * 6 / 5, time)
                line1.playTone(tonic * i, time)
            }

            line1.drain()
            line1.close()
        }
    }

    fun SourceDataLine.playTone(frequency: Double, ms: Int) {
        playTone(frequency, ms, this)
    }
}