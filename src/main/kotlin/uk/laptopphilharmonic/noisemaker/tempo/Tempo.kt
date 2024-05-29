package uk.laptopphilharmonic.noisemaker.tempo

data class Tempo(
    val bpm: Double
) {
    val beat: Int = ((60 / bpm) * 1000).toInt()
    val halfBeat: Int = ((30 / bpm) * 1000).toInt()
    val thirdBeat: Int = ((20 / bpm) * 1000).toInt()
    val quarterBeat: Int = ((15 / bpm) * 1000).toInt()
}

val Int.bpm get() = Tempo(this.toDouble())
val Long.bpm get() = Tempo(this.toDouble())
val Float.bpm get() = Tempo(this.toDouble())
val Double.bpm get() = Tempo(this)
