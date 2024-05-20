package uk.laptopphilharmonic.noisemaker

data class Frequency(
    val hz: Double
) {
    fun overtone(multiple: Double): Frequency = Frequency(this.hz * multiple)
    fun overtone(multiple: Int): Frequency = this.overtone(multiple.toDouble())
    fun undertone(divisor: Double): Frequency = Frequency(this.hz / divisor)
    fun undertone(divisor: Int): Frequency = this.undertone(divisor.toDouble())
}

val Int.hz get() = Frequency(this.toDouble())
val Long.hz get() = Frequency(this.toDouble())
val Float.hz get() = Frequency(this.toDouble())
val Double.hz get() = Frequency(this)