package uk.laptopphilharmonic.noisemaker.frequency

data class Frequency(
    val hz: Double
) {
    fun overtone(multiple: Double): Frequency = Frequency(this.hz * multiple)
    fun overtone(multiple: Int): Frequency = this.overtone(multiple.toDouble())
    fun undertone(divisor: Double): Frequency = Frequency(this.hz / divisor)
    fun undertone(divisor: Int): Frequency = this.undertone(divisor.toDouble())

    fun inOctave(octaveRange: OctaveRange): Frequency {
        return if (hz < octaveRange.lower.hz) {
            println("$hz < ${octaveRange.lower.hz}, x2")
           this.overtone(2.0).inOctave(octaveRange)
        } else if (hz > octaveRange.upper.hz) {
            println("$hz > ${octaveRange.upper.hz}, /2")
            this.undertone(2.0).inOctave(octaveRange)
        } else this
    }
}

val Int.hz get() = Frequency(this.toDouble())
val Long.hz get() = Frequency(this.toDouble())
val Float.hz get() = Frequency(this.toDouble())
val Double.hz get() = Frequency(this)