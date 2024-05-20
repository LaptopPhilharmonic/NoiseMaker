package uk.laptopphilharmonic.noisemaker

data class Tempo(
    val bpm: Double
) {
    val beatMillis: Double = (60 / bpm) * 1000
}

fun Int.bpm() = Tempo(this.toDouble())
fun Long.bpm() = Tempo(this.toDouble())
fun Float.bpm() = Tempo(this.toDouble())
fun Double.bpm() = Tempo(this)
