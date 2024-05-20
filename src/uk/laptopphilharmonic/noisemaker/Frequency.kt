package uk.laptopphilharmonic.noisemaker

data class Frequency(
    val hz: Double
) {
    fun overtone(multiple: Double): Frequency = Frequency(this.hz * multiple)
    fun undertone(divisor: Double): Frequency = Frequency(this.hz / divisor)
}

val Int.hz get() = Frequency(this.toDouble())
val Long.hz get() = Frequency(this.toDouble())
val Float.hz get() = Frequency(this.toDouble())
val Double.hz get() = Frequency(this)