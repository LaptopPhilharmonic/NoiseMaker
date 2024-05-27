package uk.laptopphilharmonic.noisemaker.frequency

open class Fifth(baseFrequency: Frequency) {
    val tonic = baseFrequency
    val octaveRange = OctaveRange(tonic)
    val dominant = (tonic * 3).inOctave(octaveRange)
}

interface Triad {
    val tonic: Frequency
    val dominant: Frequency
    val mediant: Frequency
    val all: List<Frequency>
    operator fun times(i: Int): Triad
    operator fun div(i: Int): Triad
    fun random(): Frequency
}

class MajorTriad(baseFrequency: Frequency): Fifth(baseFrequency), Triad {
    override val mediant = (tonic * 5).inOctave(octaveRange)
    fun toMinor() = MinorTriad(this.tonic)
    override operator fun times(i: Int) = MajorTriad(this.tonic * i)
    override operator fun div(i: Int) = MajorTriad(this.tonic / i)
    override val all: List<Frequency> = listOf(this.tonic, this.dominant, this.mediant)
    override fun random(): Frequency = this.all.random()
}

class MinorTriad(baseFrequency: Frequency): Fifth(baseFrequency), Triad {
    override val mediant = (dominant / 5).inOctave(octaveRange)
    fun toMajor() = MajorTriad(this.tonic)
    override operator fun times(i: Int) = MinorTriad(this.tonic * i)
    override operator fun div(i: Int) = MinorTriad(this.tonic / i)
    override val all: List<Frequency> = listOf(this.tonic, this.dominant, this.mediant)
    override fun random(): Frequency = this.all.random()
}