package uk.laptopphilharmonic.noisemaker.frequency

/**
 * A frequency in hz (how many times per second your wave should loop). In standard modern tuning, A4 is 440hz
 * @param hz - The frequency. Must be > 0 to avoid math problems
 */
data class Frequency(
    val hz: Double
) {
    init {
        require(hz > 0.0)
    }

    val waveLengthMillis = 2000.0 / hz // Why is it 2 seconds and not 1?

    operator fun times(i: Int) = Frequency(this.hz * i)
    operator fun times(d: Double) = Frequency(this.hz * d)
    operator fun div(i: Int) = Frequency(this.hz / i)
    operator fun div(d: Double) = Frequency(this.hz / d)

    /**
     * Returns this frequency transposed up/down octaves until it's within the range provided.
     * */
    fun inOctave(octaveRange: OctaveRange): Frequency {
        return if (hz < octaveRange.lower.hz) {
            (this * 2).inOctave(octaveRange)
        } else if (hz > octaveRange.upper.hz) {
            (this / 2).inOctave(octaveRange)
        } else this
    }
}

val Int.hz get() = Frequency(this.toDouble())
val Long.hz get() = Frequency(this.toDouble())
val Float.hz get() = Frequency(this.toDouble())
val Double.hz get() = Frequency(this)