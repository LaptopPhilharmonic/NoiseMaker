package uk.laptopphilharmonic.noisemaker

import uk.laptopphilharmonic.noisemaker.frequency.Frequency
import uk.laptopphilharmonic.noisemaker.frequency.OctaveRange
import kotlin.random.Random

/** Creates a musical perfect fifth interval (not 12-tone) to go with the baseFrequency provided */
open class Fifth(baseFrequency: Frequency) {
    /** The base frequency of the interval */
    val tonic = baseFrequency
    /** A single OctaveRange from the base frequency */
    val octaveRange = OctaveRange(tonic)
    /** The perfect 5th (i.e. the base frequency * 1.5) */
    val dominant = (tonic * 3).inOctave(octaveRange)
}

/** A data structure for standard three note chords */
interface Triad {
    /** The root note of the chord, will be the base frequency supplied */
    val tonic: Frequency
    /** The dominant (will be a perfect 5th) */
    val dominant: Frequency
    /** The mediant Note (could be major or minor) */
    val mediant: Frequency
    /** All Notes in this Triad */
    val all: List<Frequency>
    /** Creates a new Triad with all notes multiplied by the supplied integer */
    operator fun times(i: Int): Triad
    /** Creates a new Triad with all notes divided by the supplied integer */
    operator fun div(i: Int): Triad
    /** Returns a random note from the Triad */
    fun random(): Frequency
}

/**
 * All Triad implementation should inherit from this
 * @param baseFrequency - this will be the tonic note that the others are derived from
 */
abstract class AbstractTriad(baseFrequency: Frequency) : Fifth(baseFrequency), Triad {
    override val all: List<Frequency> get() = listOf(this.tonic, this.dominant, this.mediant)
    override fun random(): Frequency = this.all.random()
}

/** A triad with the mediant set to a major third (1.25 * tonic frequency) */
class MajorTriad(baseFrequency: Frequency): AbstractTriad(baseFrequency), Triad {
    override val mediant = (tonic * 5).inOctave(octaveRange)
    /** Creates a new MinorTriad with the same tonic frequency as this */
    fun toMinor() = MinorTriad(this.tonic)
    override operator fun times(i: Int) = MajorTriad(this.tonic * i)
    override operator fun div(i: Int) = MajorTriad(this.tonic / i)
}

/** A triad with the mediant set to a minor third (1.2 * tonic frequency) */
class MinorTriad(baseFrequency: Frequency): AbstractTriad(baseFrequency), Triad {
    override val mediant = (dominant / 5).inOctave(octaveRange)
    fun toMajor() = MajorTriad(this.tonic)
    override operator fun times(i: Int) = MinorTriad(this.tonic * i)
    override operator fun div(i: Int) = MinorTriad(this.tonic / i)
}

/**
 * Get a random overtone (exact integer multiple) of this Frequency
 * @param limit - the largest number to multiply by
 */
fun Frequency.randomOvertone(limit: Int = 8): Frequency {
    return this * Random.nextInt(1, limit)
}

/**
 * Get a random undertone (the frequency divided by an integer) of this Frequency
 * @param limit - the largest number to divide by
 */
fun Frequency.randomUndertone(limit: Int = 8): Frequency {
    return this / Random.nextInt(1, limit)
}