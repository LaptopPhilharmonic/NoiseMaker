package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.frequency.Frequency
import uk.laptopphilharmonic.noisemaker.frequency.OctaveRange
import kotlin.random.Random

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

abstract class AbstractTriad(baseFrequency: Frequency) : Fifth(baseFrequency), Triad {
    override val all: List<Frequency> get() = listOf(this.tonic, this.dominant, this.mediant)
    override fun random(): Frequency = this.all.random()
}

class MajorTriad(baseFrequency: Frequency): AbstractTriad(baseFrequency), Triad {
    override val mediant = (tonic * 5).inOctave(octaveRange)
    fun toMinor() = MinorTriad(this.tonic)
    override operator fun times(i: Int) = MajorTriad(this.tonic * i)
    override operator fun div(i: Int) = MajorTriad(this.tonic / i)
}

class MinorTriad(baseFrequency: Frequency): AbstractTriad(baseFrequency), Triad {
    override val mediant = (dominant / 5).inOctave(octaveRange)
    fun toMajor() = MajorTriad(this.tonic)
    override operator fun times(i: Int) = MinorTriad(this.tonic * i)
    override operator fun div(i: Int) = MinorTriad(this.tonic / i)
}

fun Frequency.randomOvertone(limit: Int = 8): Frequency {
    return this * Random.nextInt(1, limit)
}

fun Frequency.randomUndertone(limit: Int = 8): Frequency {
    return this / Random.nextInt(1, limit)
}