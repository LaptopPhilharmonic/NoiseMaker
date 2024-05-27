package uk.laptopphilharmonic.noisemaker.frequency

data class Frequency(
    val hz: Double
) {
    init {
        require(hz > 0.0)
    }

    fun overtone(multiple: Double): Frequency = Frequency(this.hz * multiple)
    fun overtone(multiple: Int): Frequency = this.overtone(multiple.toDouble())
    fun undertone(divisor: Double): Frequency = Frequency(this.hz / divisor)
    fun undertone(divisor: Int): Frequency = this.undertone(divisor.toDouble())

    operator fun times(i: Int) = Frequency(this.hz * i)
    operator fun times(d: Double) = Frequency(this.hz * d)
    operator fun div(i: Int) = Frequency(this.hz / i)
    operator fun div(d: Double) = Frequency(this.hz / d)

    fun inOctave(octaveRange: OctaveRange): Frequency {
        return if (hz < octaveRange.lower.hz) {
           this.overtone(2.0).inOctave(octaveRange)
        } else if (hz > octaveRange.upper.hz) {
            this.undertone(2.0).inOctave(octaveRange)
        } else this
    }
}

val Int.hz get() = Frequency(this.toDouble())
val Long.hz get() = Frequency(this.toDouble())
val Float.hz get() = Frequency(this.toDouble())
val Double.hz get() = Frequency(this)